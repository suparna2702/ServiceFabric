package com.similan.service.impl.document.viewer;

import java.io.IOException;

import lombok.Cleanup;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.asset.dto.basic.AssetMediaType;
import com.similan.service.api.asset.dto.basic.AssetMetadataDto;
import com.similan.service.api.asset.dto.basic.AssetType;
import com.similan.service.internal.api.asset.io.TemporaryFile;

public abstract class ChainedDocumentViewer extends AbstractDocumentViewer {
  private final AssetType chainedType;
  private DocumentViewer chainedViewer;

  public ChainedDocumentViewer(String id, AssetType chainedType) {
    super(id);
    this.chainedType = chainedType;
  }
  
  public void setChainedViewer(DocumentViewer chainedViewer) {
    this.chainedViewer = chainedViewer;
  }
  
  @Override
  public void prepare(AssetInfoDto info, TemporaryFile file,
      DocumentViewerComponentFactory componentFactory) throws IOException {

    @Cleanup
    TemporaryFile intermediateFile = componentFactory.createTemporaryFile();
    prepareIntermediate(info, file, intermediateFile);
    AssetInfoDto intermediateInfo = createIntermediateInfo(info);
    chainedViewer.prepare(intermediateInfo, intermediateFile, componentFactory);
  }

  private AssetInfoDto createIntermediateInfo(AssetInfoDto info) {
    String filename = chainedType.getDefaultFilename(info.getFilename());
    AssetMediaType mediaType = chainedType.getDefaultMediaType();
    AssetType type = chainedType;
    String contentType = mediaType.getDescriptor();
    AssetMetadataDto metadata = new AssetMetadataDto(contentType, null);
    return new AssetInfoDto(filename, mediaType, type, metadata);
  }

  protected abstract void prepareIntermediate(AssetInfoDto info,
      TemporaryFile file, TemporaryFile intermediateFile) throws IOException;
}
