package com.similan.service.internal.impl.asset;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.similan.domain.entity.asset.Asset;
import com.similan.domain.entity.asset.AssetAdditionalMetadata;
import com.similan.domain.entity.asset.AssetInfo;
import com.similan.domain.entity.asset.AssetMetadata;
import com.similan.service.api.asset.dto.basic.AssetAdditionalMetadataDto;
import com.similan.service.api.asset.dto.basic.AssetDto;
import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.asset.dto.basic.AssetMediaType;
import com.similan.service.api.asset.dto.basic.AssetMetadataDto;
import com.similan.service.api.asset.dto.basic.AssetType;
import com.similan.service.impl.Marshaller;

@Component
public class AssetMarshaller extends Marshaller {
  AssetDto marshallAsset(Asset asset) {
    String explicitFilename = asset.getExplicitFilename();
    AssetInfo providedInfo = asset.getProvidedInfo();
    AssetInfoDto providedInfoDto = marshallInfo(providedInfo);
    AssetInfo detectedInfo = asset.getDetectedInfo();
    AssetInfoDto detectedInfoDto = marshallInfo(detectedInfo);
    AssetDto assetDto = new AssetDto(explicitFilename, providedInfoDto,
        detectedInfoDto);
    return assetDto;
  }

  public AssetInfoDto marshallInfo(AssetInfo info) {
    String filename = info.getFilename();
    AssetMediaType mediaType = info.getMediaType();
    AssetType type = info.getType();
    AssetMetadata metadata = info.getMetadata();
    AssetMetadataDto metadataDto = marshallMetadata(metadata);
    AssetInfoDto infoDto = new AssetInfoDto(filename, mediaType, type,
        metadataDto);
    return infoDto;
  }

  private AssetMetadataDto marshallMetadata(AssetMetadata metadata) {
    String contentType = metadata.getContentType();
    String contentDisposition = metadata.getContentDisposition();
    List<AssetAdditionalMetadata> additionals = metadata.getAdditionals();
    List<AssetAdditionalMetadataDto> additionalDtos = marshallAdditionals(additionals);

    AssetMetadataDto metadataDto = new AssetMetadataDto(contentType,
        contentDisposition, additionalDtos);

    return metadataDto;
  }

  private List<AssetAdditionalMetadataDto> marshallAdditionals(
      List<AssetAdditionalMetadata> additionals) {
    if (additionals == null) {
      return new LinkedList<AssetAdditionalMetadataDto>();
    }
    List<AssetAdditionalMetadataDto> additionalDtos = new ArrayList<AssetAdditionalMetadataDto>(
        additionals.size());
    for (AssetAdditionalMetadata additional : additionals) {
      AssetAdditionalMetadataDto additionalDto = marshallAdditional(additional);
      additionalDtos.add(additionalDto);
    }
    return additionalDtos;
  }

  private AssetAdditionalMetadataDto marshallAdditional(
      AssetAdditionalMetadata additional) {
    String name = additional.getName();
    String value = additional.getValue();
    AssetAdditionalMetadataDto additionalDto = new AssetAdditionalMetadataDto(
        name, value);
    return additionalDto;
  }

}
