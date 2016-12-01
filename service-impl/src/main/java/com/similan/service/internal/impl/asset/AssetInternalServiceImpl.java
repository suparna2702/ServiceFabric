package com.similan.service.internal.impl.asset;

import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.similan.domain.entity.asset.Asset;
import com.similan.domain.entity.asset.AssetAdditionalMetadata;
import com.similan.domain.entity.asset.AssetDetectionPolicy;
import com.similan.domain.entity.asset.AssetInfo;
import com.similan.domain.entity.asset.AssetMetadata;
import com.similan.domain.repository.asset.AssetAdditionalMetadataRepository;
import com.similan.domain.repository.asset.AssetMetadataRepository;
import com.similan.domain.repository.asset.AssetRepository;
import com.similan.service.api.asset.AssetStream;
import com.similan.service.api.asset.NewAssetStream;
import com.similan.service.api.asset.dto.basic.AssetAdditionalMetadataDto;
import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.asset.dto.basic.AssetMediaType;
import com.similan.service.api.asset.dto.basic.AssetMetadataDto;
import com.similan.service.api.asset.dto.basic.AssetType;
import com.similan.service.api.asset.dto.error.AssetErrorCode;
import com.similan.service.api.asset.dto.error.AssetException;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.internal.api.asset.AssetInternalService;
import com.similan.service.internal.api.asset.io.NewAsset;
import com.similan.service.internal.api.asset.io.TemporaryFile;
import com.similan.service.internal.impl.asset.store.AssetStore;
import com.similan.service.internal.impl.asset.store.AssetStore.StoredAssetMetadata;

@Slf4j
@Service("assetInternalService")
public class AssetInternalServiceImpl extends ServiceImpl implements
    AssetInternalService {
  @Autowired
  AssetRepository assetRepository;
  @Autowired
  AssetMetadataRepository metadataRepository;
  @Autowired
  AssetAdditionalMetadataRepository additionalMetadataRepository;
  @Autowired
  List<AssetStore> stores;
  @Autowired
  BasicAssetMetadataDetector basicDetector;
  @Autowired
  ExhaustiveAssetMetadataDetector exhaustiveDetector;
  @Autowired
  AssetFilenameDetector filenameDetector;
  @Autowired
  AssetMediaTypeFinder mediaTypeFinder;
  @Autowired
  AssetTypeFinder typeFinder;
  @Autowired
  SocialActorMarshaller actorMarshaller;
  @Autowired
  private AssetMarshaller assetMarshaller;
  
  AssetStore store;
  
  @Setter
  @Getter
  String storeId;

  Map<String, AssetStore> storesById = new HashMap<String, AssetStore>();

  @PostConstruct
  public void initialize() {
    checkState(StringUtils.isNotBlank(storeId), "Store id must be set.");
    for (AssetStore store : stores) {
      String storeId = store.getId();
      if (storesById.put(storeId, store) != null) {
        throw new IllegalArgumentException("Duplicate store id:  " + storeId);
      }
    }
    store = storesById.get(storeId);
    checkState(store != null, "Store with id " + storeId + " not found.");
  }

  private AssetInfoDto infer(String knownFilename, AssetMetadataDto metadata) {
    String filename = filenameDetector.detect(knownFilename, metadata);
    String contentType = metadata.getContentType();
    AssetMediaType mediaType = mediaTypeFinder.find(contentType);
    AssetType type = typeFinder.find(mediaType, filename);
    return new AssetInfoDto(filename, mediaType, type, metadata);
  }

  @Override
  @Transactional(propagation = Propagation.MANDATORY)
  public Asset create(NewAsset newAsset, NewAssetStream assetStream,
      AssetProcessingTask... tasks) {
    Asset asset = createAsset(newAsset);
    TemporaryFile file = null;
    try {
      file = createTemporaryFile();
      @Cleanup
      OutputStream output = file.openOutput();
      IOUtils.copy(assetStream.getInput(), output);
      asset.setSize(file.get().length());
      complete(asset, file, tasks);
    } catch (IOException e) {
      throw new AssetException(AssetErrorCode.ERROR_UPLOADING_ASSET,
          "Error uploading asset: " + asset.getId(), e);
    } finally {
      if (file != null) {
        file.close();
      }
    }
    return asset;
  }

  private void complete(Asset asset, TemporaryFile file,
      AssetProcessingTask... tasks) {
    detect(asset, file);
    upload(asset, file);
    process(asset, file, tasks);
  }

  private void upload(Asset asset, TemporaryFile file) {
    final long id = asset.getId();
    try {
      String category = asset.getCategory();
      String storeId = asset.getStoreId();
      final AssetStore store = storesById.get(storeId);
      StoredAssetMetadata storedAssetMetadata = buildStoredMetadata(asset);
      final String storeKey = store.create(category, file, storedAssetMetadata);
      asset.setStoreKey(storeKey);
      TransactionSynchronizationManager
          .registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {
              if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                delete(id, store, storeKey, true);
              }
            }
          });
    } catch (IOException e) {
      throw new AssetException(AssetErrorCode.ERROR_STORING_ASSET,
          "Error storing asset: " + id, e);
    }
  }

  private StoredAssetMetadata buildStoredMetadata(Asset asset) {
    AssetInfo detectedInfo = asset.getDetectedInfo();
    AssetInfo providedInfo = asset.getProvidedInfo();
    String filename = null;
    AssetMediaType mediaType = null;
    if (detectedInfo != null) {
      // try to use detected filename and media type
      filename = detectedInfo.getFilename();
      mediaType = detectedInfo.getMediaType();
    }
    if (providedInfo != null) {
      if (StringUtils.isBlank(filename)) {
        // if no detected filename, try provided one
        filename = providedInfo.getFilename();
      }
      if (mediaType == null
          && asset.getDetectionPolicy() == AssetDetectionPolicy.TRUSTED) {
        // if mediatype not detected, but provided one is trusted, try that
        mediaType = providedInfo.getMediaType();
      }
    }
    if (StringUtils.isBlank(filename)) {
      if (detectedInfo != null) {
        // if filename still null try detected default.
        filename = detectedInfo.getType().getDefaultFilename(null);
      }
      if (providedInfo != null && StringUtils.isBlank(filename)) {
        // if filename still null try provided default.
        filename = providedInfo.getType().getDefaultFilename(null);
      }
      if (StringUtils.isBlank(filename)) {
        // just use this
        filename = "unnamed";
      }
    }
    if (mediaType == null) {
      // just use this
      mediaType = AssetMediaType.APPLICATION_OCTET_STREAM;
    }
    return StoredAssetMetadata.builder()
        .attachment(asset.isAttachment())
        .filename(filename)
        .contentType(mediaType.getDescriptor())
        .build();
  }

  private Asset createAsset(NewAsset newAssetDto) {
    String storeId = store.getId();

    String explicitFilename = newAssetDto.getExplicitFilename();
    AssetMetadataDto providedMetadataDto = newAssetDto.getProvidedMetadata();
    AssetInfoDto providedInfoDto = infer(explicitFilename, providedMetadataDto);

    AssetDetectionPolicy detectionPolicy = newAssetDto.getDetectionPolicy();
    String category = newAssetDto.getCategory();
    AssetInfo providedInfo = createAssetInfo(providedInfoDto);
    Asset asset = assetRepository.create(detectionPolicy, category,
        explicitFilename, providedInfo, storeId, newAssetDto.getAttachment());
    assetRepository.save(asset);

    return asset;
  }

  private AssetInfo createAssetInfo(AssetInfoDto infoDto) {
    AssetMetadataDto metadataDto = infoDto.getMetadata();
    AssetMetadata metadata = createMetadata(metadataDto);
    String filename = infoDto.getFilename();
    AssetMediaType mediaType = infoDto.getMediaType();
    AssetType type = infoDto.getType();
    AssetInfo info = assetRepository.createInfo(filename, mediaType, type,
        metadata);
    return info;
  }

  private AssetMetadata createMetadata(AssetMetadataDto metadataDto) {
    String contentType = metadataDto.getContentType();
    String contentDisposition = metadataDto.getContentDisposition();
    AssetMetadata metadata = metadataRepository.create(contentType,
        contentDisposition);
    metadataRepository.save(metadata);

    List<AssetAdditionalMetadataDto> additionalDtos = metadataDto
        .getAdditionals();
    createAdditionalMetadata(metadata, additionalDtos);
    return metadata;
  }

  private List<AssetAdditionalMetadata> createAdditionalMetadata(
      AssetMetadata metadata, List<AssetAdditionalMetadataDto> additionalDtos) {
    List<AssetAdditionalMetadata> additionals = new ArrayList<AssetAdditionalMetadata>(
        additionalDtos.size());
    for (AssetAdditionalMetadataDto additionalDto : additionalDtos) {
      AssetAdditionalMetadata additional = createAdditionalMetadata(metadata,
          additionalDto);
      additionals.add(additional);
    }
    return additionals;
  }

  private AssetAdditionalMetadata createAdditionalMetadata(
      AssetMetadata metadata, AssetAdditionalMetadataDto additionalDto) {
    String name = additionalDto.getName();
    String value = additionalDto.getValue();
    AssetAdditionalMetadata additional = additionalMetadataRepository.create(
        metadata, name, value);
    additionalMetadataRepository.save(additional);
    return additional;
  }

  private void detect(Asset asset, TemporaryFile file) {
    AssetDetectionPolicy detection = asset.getDetectionPolicy();

    AssetInfo providedInfo = asset.getProvidedInfo();
    AssetInfoDto providedInfoDto = assetMarshaller
        .marshallInfo(providedInfo);

    AssetMetadataDto detectedMetadataDto;
    try {
      detectedMetadataDto = doDetect(asset, file, detection, providedInfoDto);
    } catch (IOException e) {
      throw new AssetException(AssetErrorCode.ERROR_DETECTING_ASSET,
          "Error while detecting document " + asset.getId(), e);
    }
    AssetInfoDto detectedInfoDto;
    if (detectedMetadataDto == null) {
      detectedInfoDto = null;
    } else {
      String explicitFilename = asset.getExplicitFilename();
      String knownFilename;
      if (explicitFilename == null) {
        knownFilename = providedInfo.getFilename();
      } else {
        knownFilename = explicitFilename;
      }
      detectedInfoDto = infer(knownFilename, detectedMetadataDto);
    }

    detected(asset, detectedInfoDto);
  }

  private AssetMetadataDto doDetect(Asset asset, TemporaryFile file,
      AssetDetectionPolicy detectionPolicy, AssetInfoDto providedInfoDto)
      throws IOException {
    switch (detectionPolicy) {
    case TRUSTED:
      return providedInfoDto.getMetadata();
    case BASIC:
      return doDetectBasic(asset, file, providedInfoDto);
    case EXHAUSTIVE:
      return doDetectExaustive(asset, file, providedInfoDto);
    default:
      throw new IllegalArgumentException("Invalid detection policy: "
          + detectionPolicy);
    }
  }

  private AssetMetadataDto doDetectExaustive(Asset asset, TemporaryFile file,
      AssetInfoDto providedInfoDto) throws IOException {
    AssetMetadataDto detectedMetadataDto = exhaustiveDetector.detectMetadata(
        providedInfoDto, file);
    return detectedMetadataDto;
  }

  private AssetMetadataDto doDetectBasic(Asset asset, TemporaryFile file,
      AssetInfoDto providedInfoDto) throws IOException {
    String contentType = basicDetector.detect(providedInfoDto, file);
    AssetMetadataDto detectedMetadataDto = new AssetMetadataDto(contentType,
        null, null);
    return detectedMetadataDto;
  }

  private void detected(Asset asset, AssetInfoDto detectedInfoDto) {
    if (detectedInfoDto != null) {
      AssetInfo detectedInfo = createAssetInfo(detectedInfoDto);
      asset.setDetectedInfo(detectedInfo);
    }
  }

  private void process(Asset asset, TemporaryFile file,
      AssetProcessingTask... tasks) {
    for (AssetProcessingTask task : tasks) {
      try {
        task.process(asset, file);
      } catch (IOException e) {
        throw new AssetException(AssetErrorCode.ERROR_PROCESSING_ASSET,
            "Error while running asset processing task " + task + " for asset "
                + asset.getId(), e);
      }
    }
  }

  @Transactional(propagation = Propagation.MANDATORY)
  private Asset getEntity(long id) {
    Asset asset = assetRepository.findOne(id);
    if (asset == null) {
      throw new AssetException(AssetErrorCode.ASSET_NOT_FOUND, "Asset " + id
          + "  does not exist");
    }
    return asset;
  }

  private InputStream openStream(Asset asset) throws IOException {
    String storeId = asset.getStoreId();
    String storeKey = asset.getStoreKey();
    AssetStore store = storesById.get(storeId);
    InputStream stream = store.get(storeKey);
    return stream;
  }

  private InputStream doRetrieve(Asset asset) {
    try {
      return openStream(asset);
    } catch (IOException e) {
      throw new AssetException(AssetErrorCode.ERROR_RETRIEVING_ASSET,
          "Could not retrieve asset: " + asset.getId(), e);
    }
  }

  @Override
  @Transactional(propagation = Propagation.MANDATORY)
  public AssetStream retrieve(Asset asset) {
    AssetInfo info = asset.getDetectedInfo();
    AssetInfoDto infoDto = assetMarshaller.marshallInfo(
        info);
    InputStream input = doRetrieve(asset);
    return new AssetStream(infoDto, input);
  }

  @Transactional(propagation = Propagation.MANDATORY)
  private void delete(long id) {
    Asset asset = getEntity(id);
    delete(asset);
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public void delete(Asset asset) {
    final long id = asset.getId();
    String storeId = asset.getStoreId();
    final AssetStore store = storesById.get(storeId);
    if (store == null) {
      throw new AssetException(AssetErrorCode.ERROR_DELETING_ASSET,
          "Deleting asset from removed store: " + storeId + ", asset: "
              + asset.getId());
    }
    final String storeKey = asset.getStoreKey();

    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {

          public void afterCommit() {
            delete(id, store, storeKey, false);
          }
        });
  }

  private void delete(final long id, final AssetStore store,
      final String storeKey, boolean quietly) {
    try {
      store.delete(storeKey);
    } catch (IOException e) {
      String message = "Error deleting asset " + id + " from store "
          + store.getId() + "with key " + storeKey + ": " + e;
      log.error(message, e);
      if (!quietly) {
        throw new AssetException(AssetErrorCode.ERROR_DELETING_ASSET, message,
            e);
      }
    }
  }

  @Override
  public TemporaryFile createTemporaryFile() throws IOException {
    return new TemporaryFile(File.createTempFile("asset", ".tmp"));
  }
}
