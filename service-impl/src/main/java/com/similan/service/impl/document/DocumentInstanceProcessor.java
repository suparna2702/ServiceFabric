package com.similan.service.impl.document;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.asset.Asset;
import com.similan.domain.entity.asset.AssetDetectionPolicy;
import com.similan.domain.entity.asset.AssetInfo;
import com.similan.domain.entity.document.DocumentInstance;
import com.similan.domain.entity.document.DocumentPage;
import com.similan.domain.entity.document.DocumentViewerItem;
import com.similan.domain.repository.document.DocumentInstanceRepository;
import com.similan.domain.repository.document.DocumentPageRepository;
import com.similan.domain.repository.document.DocumentViewerItemRepository;
import com.similan.service.api.asset.AssetStream;
import com.similan.service.api.asset.NewAssetStream;
import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.asset.dto.basic.AssetMediaType;
import com.similan.service.api.asset.dto.basic.AssetMetadataDto;
import com.similan.service.api.asset.dto.basic.AssetType;
import com.similan.service.api.document.dto.error.DocumentErrorCode;
import com.similan.service.api.document.dto.error.DocumentException;
import com.similan.service.impl.document.extractor.DocumentTextExtractor;
import com.similan.service.impl.document.icon.DocumentIconGenerator;
import com.similan.service.impl.document.viewer.DocumentViewer;
import com.similan.service.impl.document.viewer.DocumentViewerComponentFactory;
import com.similan.service.internal.api.asset.AssetInternalService;
import com.similan.service.internal.api.asset.io.NewAsset;
import com.similan.service.internal.api.asset.io.TemporaryFile;
import com.similan.service.internal.impl.asset.AssetMarshaller;
import com.similan.service.internal.impl.asset.AssetProcessingTask;

@Slf4j
@Component
class DocumentInstanceProcessor {
  private static final String ASSET_CATEGORY = "document";

  private final class ViewerTask implements AssetProcessingTask {
    private final long id;

    private ViewerTask(long id) {
      this.id = id;
    }

    @Override
    public void process(Asset asset, TemporaryFile file) throws IOException {
      DocumentInstanceProcessor.this.processViewer(id, asset, file);
    }

    @Override
    public String toString() {
      return "ViewerTask[document instance " + id + "]";
    }
  }

  private final class TextTask implements AssetProcessingTask {
    private final long id;

    private TextTask(long id) {
      this.id = id;
    }

    @Override
    public void process(Asset asset, TemporaryFile file) throws IOException {
      DocumentInstanceProcessor.this.processText(id, asset, file);
    }

    @Override
    public String toString() {
      return "TextTask[document instance " + id + "]";
    }
  }

  private final class IconTask implements AssetProcessingTask {
    private final long id;

    private IconTask(long id) {
      this.id = id;
    }

    @Override
    public void process(Asset asset, TemporaryFile file) throws IOException {
      DocumentInstanceProcessor.this.processIcon(id, asset);
    }

    @Override
    public String toString() {
      return "IconTask[document instance " + id + "]";
    }
  }

  private final class OriginalTask implements AssetProcessingTask {
    private final long id;

    private OriginalTask(long id) {
      this.id = id;
    }

    @Override
    public void process(Asset asset, TemporaryFile file) throws IOException {
      DocumentInstanceProcessor.this.processOriginal(id, asset);
    }

    @Override
    public String toString() {
      return "OrignalTask[document instance " + id + "]";
    }
  }

  public abstract class DerivedAssetFactoryImpl implements DerivedAssetFactory {

    private final DocumentInstance documentInstance;

    private Asset asset = null;

    public DerivedAssetFactoryImpl(DocumentInstance documentInstance) {
      this.documentInstance = documentInstance;
    }

    @Override
    public TemporaryFile createTemporaryFile() throws IOException {
      return assetService.createTemporaryFile();
    }
    
    @Override
    public void create(InputStream input) {
      if (asset != null) {
        throw new IllegalStateException("Already opened");
      }
      asset = doCreate(input);
      created(asset);

    }

    private Asset doCreate(InputStream input) {
      String contentType = getContentType();
      String filename = getFilename();
      return createDerivedAsset(contentType, filename, input);
    }

    protected DocumentInstance getDocumentInstance() {
      return documentInstance;
    }

    private String getFilename() {
      DocumentInstance documentInstance = getDocumentInstance();
      Asset originalAsset = documentInstance.getOriginalAsset();
      AssetInfo detectedInfo = originalAsset.getDetectedInfo();
      String filename = detectedInfo.getFilename();
      if (filename == null) {
        filename = "unnamed-document";
      }
      filename += getSuffix();
      return filename;
    }

    private String getContentType() {
      AssetMediaType type = getType();
      return type.getDescriptor();
    }

    protected abstract String getSuffix();

    protected abstract AssetMediaType getType();

    protected abstract void created(Asset asset);

  }

  public class IconAssetFactoryImpl extends DerivedAssetFactoryImpl {

    public IconAssetFactoryImpl(DocumentInstance documentInstance) {
      super(documentInstance);
    }

    @Override
    protected String getSuffix() {
      return ".icon.png";
    }

    @Override
    protected AssetMediaType getType() {
      return AssetMediaType.IMAGE_PNG;
    }

    @Override
    protected void created(Asset asset) {
      DocumentInstance documentInstance = getDocumentInstance();
      documentInstance.setIconAsset(asset);
    }

  }

  public class TextAssetFactoryImpl extends DerivedAssetFactoryImpl {

    public TextAssetFactoryImpl(DocumentInstance documentInstance) {
      super(documentInstance);
    }

    @Override
    protected String getSuffix() {
      return ".extract.txt";
    }

    @Override
    protected AssetMediaType getType() {
      return AssetMediaType.TEXT_PLAIN;
    }

    @Override
    protected void created(Asset asset) {
      DocumentInstance documentInstance = getDocumentInstance();
      documentInstance.setTextAsset(asset);
    }

  }

  public class DocumentViewerComponentFactoryImpl implements
      DocumentViewerComponentFactory {

    private final DocumentInstance document;

    private int pageCount = 0;

    public DocumentViewerComponentFactoryImpl(DocumentInstance document) {
      this.document = document;
    }
    @Override
    public TemporaryFile createTemporaryFile() throws IOException {
      return assetService.createTemporaryFile();
    }

    private String getNextPageFilename() {
      return "page-" + (pageCount + 1);
    }

    private String getNextPageThumbnailFilename() {
      return "page-thumbnail-" + (pageCount + 1);
    }

    public void createPage(String pageFilename, String pageContentType,
        InputStream pageInput, String thumbnailFilename,
        String thumbnailContentType, InputStream thumbnailInput) {
      Asset pageAsset = createDerivedAsset(pageContentType, pageFilename,
          pageInput);

      Asset thumbnailAsset = null;
      if (thumbnailInput != null) {
        thumbnailAsset = createDerivedAsset(thumbnailContentType,
            thumbnailFilename, thumbnailInput);
      }
      DocumentPage page = documentPageRepository.create(document, pageCount++,
          pageAsset, thumbnailAsset);
      documentPageRepository.save(page);
    }

    @Override
    public void createPage(AssetType pageType, InputStream pageInput,
        AssetType thumbnailType, InputStream thumbnailInput) {
      createPage(pageType.getDefaultFilename(getNextPageFilename()),
          pageType.getDefaultMediaType(), pageInput,
          thumbnailType.getDefaultFilename(getNextPageThumbnailFilename()),
          thumbnailType.getDefaultMediaType(), thumbnailInput);
    }

    @Override
    public void createPage(String pageFilename, AssetMediaType pageMediaType,
        InputStream pageInput, String thumbnailFilename,
        AssetMediaType thumbnailMediaType, InputStream thumbnailInput) {
      createPage(pageFilename, pageMediaType.getDescriptor(), pageInput,
          thumbnailFilename, thumbnailMediaType.getDescriptor(), thumbnailInput);
    }

    @Override
    public void createPage(AssetType pageType, InputStream pageInput) {
      createPage(pageType.getDefaultFilename(getNextPageFilename()),
          pageType.getDefaultMediaType(), pageInput);
    }

    @Override
    public void createPage(String pageFilename, AssetMediaType pageMediaType,
        InputStream pageInput) {
      createPage(pageFilename, pageMediaType.getDescriptor(), pageInput);
    }

    @Override
    public void createPage(String pageFilename, String pageContentType,
        InputStream pageInput) {
      createPage(pageFilename, pageContentType, pageInput, null, null, null);
    }

    private DocumentViewerItem createItem(String name, DocumentViewerItem item) {
      documentViewerItemRepository.save(item);
      return item;
    }

    @Override
    public void createAsset(String name, AssetType type, InputStream input) {
      createAsset(name, type.getDefaultFilename(name),
          type.getDefaultMediaType(), input);
    }

    @Override
    public void createAsset(String name, String filename,
        AssetMediaType mediaType, InputStream input) {
      createAsset(name, filename, mediaType.getDescriptor(), input);
    }

    public void createAsset(String name, String filename, String contentType,
        InputStream input) {
      Asset asset = createDerivedAsset(contentType, filename, input);
      DocumentViewerItem item = documentViewerItemRepository.createAsset(
          document, name, asset);
      createItem(name, item);
    }

    public void createProperty(String name, String value) {
      DocumentViewerItem item = documentViewerItemRepository.createAttribute(
          document, name, value);
      createItem(name, item);
    }
  }
  @Autowired
  private DocumentInstanceRepository documentInstanceRepository;
  @Autowired
  private DocumentPageRepository documentPageRepository;
  @Autowired
  private DocumentViewerItemRepository documentViewerItemRepository;
  @Autowired
  private AssetInternalService assetService;
  @Autowired
  private AssetMarshaller assetMarshaller;
  @Resource(name = "documentViewers")
  private Map<AssetType, DocumentViewer> viewers;
  @Resource(name = "documentTextExtractors")
  private Map<AssetType, DocumentTextExtractor> textExtractors;
  @Resource(name = "documentIconGenerators")
  private Map<AssetType, DocumentIconGenerator> iconGenerators;

  private void processText(long id, Asset originalAsset, TemporaryFile file)
      throws IOException {
    DocumentInstance documentInstance = doGetEntity(id);
    AssetInfo detectedInfo = originalAsset.getDetectedInfo();
    AssetInfoDto detectedInfoDto = assetMarshaller
        .marshallInfo(detectedInfo);

    AssetType type = detectedInfoDto.getType();
    DocumentTextExtractor extractor = textExtractors.get(type);
    if (extractor == null) {
      extractor = textExtractors.get(AssetType.UNKNOWN);
    }
    if (extractor == null) {
      return;
    }
    DerivedAssetFactory assetFactory = new TextAssetFactoryImpl(documentInstance);
    extractor.extract(detectedInfoDto, file, assetFactory);
  }

  private void processIcon(long id, Asset originalAsset) throws IOException {
    DocumentInstance documentInstance = doGetEntity(id);
    AssetInfo detectedInfo = originalAsset.getDetectedInfo();
    AssetInfoDto detectedInfoDto = assetMarshaller
        .marshallInfo(detectedInfo);

    AssetType type = detectedInfoDto.getType();
    DocumentIconGenerator generator = iconGenerators.get(type);
    if (generator == null) {
      generator = iconGenerators.get(AssetType.UNKNOWN);
    }
    if (generator == null) {
      return;
    }
    @Cleanup
    TemporaryFile firstPageInput = getFirstPage(documentInstance);
    if (firstPageInput == null) {
      return;
    }
    DerivedAssetFactory assetFactory = new IconAssetFactoryImpl(documentInstance);
    generator.generate(detectedInfoDto, firstPageInput, assetFactory);
    firstPageInput.close();
  }

  private TemporaryFile getFirstPage(DocumentInstance documentInstance) throws IOException {
    List<DocumentPage> pages = documentInstance.getPages();
    if (pages == null || pages.isEmpty()) {
      return null;
    }
    DocumentPage page = pages.get(0);
    Asset pageAsset = page.getPageAsset();
    AssetStream pageAssetStream = assetService.retrieve(pageAsset);
    TemporaryFile file = assetService.createTemporaryFile();
    @Cleanup
    OutputStream output = file.openOutput();
    @Cleanup
    InputStream input = pageAssetStream.getInput();
    IOUtils.copy(input, output);
    output.close();
    return file;
  }

  private void processOriginal(long id, Asset originalAsset) throws IOException {
    DocumentInstance documentInstance = doGetEntity(id);
    documentInstance.setOriginalAsset(originalAsset);
  }

  private void processViewer(long id, Asset originalAsset, TemporaryFile file)
      throws IOException {
    DocumentInstance documentInstance = doGetEntity(id);
    AssetInfo detectedInfo = originalAsset.getDetectedInfo();

    AssetInfoDto detectedInfoDto = assetMarshaller
        .marshallInfo(detectedInfo);

    AssetType type = detectedInfoDto.getType();
    DocumentViewer viewer = viewers.get(type);
    if (viewer == null) {
      viewer = viewers.get(AssetType.UNKNOWN);
    }
    if (viewer == null) {
      return;
    }
    documentInstance.setViewerId(viewer.getId());
    DocumentViewerComponentFactory viewerComponentFactory = new DocumentViewerComponentFactoryImpl(
        documentInstance);
    try {
      viewer.prepare(detectedInfoDto, file, viewerComponentFactory);
    } catch (IOException e) {
      log.error("Could not process document " + originalAsset.getId()
          + " of type " + type.name() + ", setting viewer to null:" + e, e);
      documentInstance.setViewerId(null);
    }
  }

  public void create(DocumentInstance documentInstance,
      String explicitFilename, NewAssetStream assetStream) {
    checkNotNull(documentInstance);
    checkNotNull(assetStream);

    long id = documentInstance.getId();
    
    AssetProcessingTask originalTask = new OriginalTask(id);
    AssetProcessingTask textTask = new TextTask(id);
    AssetProcessingTask viewerTask = new ViewerTask(id);
    AssetProcessingTask iconTask = new IconTask(id);

    NewAsset newAsset = new NewAsset(
        AssetDetectionPolicy.EXHAUSTIVE, ASSET_CATEGORY, explicitFilename,
        assetStream.getMetadata(), true);
    assetService.create(newAsset, assetStream, originalTask, textTask,
        viewerTask, iconTask);
  }

  private DocumentInstance doGetEntity(long id) {
    DocumentInstance documentInstance = documentInstanceRepository.findOne(id);
    if (documentInstance == null) {
      throw new DocumentException(
          DocumentErrorCode.DOCUMENT_INSTANCE_NOT_FOUND, "Document instance "
              + id + "  does not exist");
    }
    return documentInstance;
  }

  private Asset createDerivedAsset(String contentType, String filename,
      InputStream input) {
    String contentDisposition = null;
    AssetMetadataDto providedMetadata = new AssetMetadataDto(contentType,
        contentDisposition);
    NewAsset newAsset = new NewAsset(
        AssetDetectionPolicy.TRUSTED, ASSET_CATEGORY, filename, providedMetadata, false);
    NewAssetStream assetStream = new NewAssetStream(new AssetMetadataDto(
        contentType, contentDisposition), input);
    return assetService.create(newAsset, assetStream);
  }
}
