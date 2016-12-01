package com.similan.service.impl.document.viewer;

import java.io.IOException;
import java.io.InputStream;

import com.similan.service.api.asset.dto.basic.AssetMediaType;
import com.similan.service.api.asset.dto.basic.AssetType;
import com.similan.service.internal.api.asset.io.TemporaryFile;

public interface DocumentViewerComponentFactory {
  TemporaryFile createTemporaryFile() throws IOException;
  
  void createPage(AssetType pageType, InputStream pageInput);

  void createPage(String pageFilename, String pageContentType, InputStream pageInput);

  void createPage(String pageFilename, AssetMediaType pageMediaType, InputStream pageInput);

  void createPage(AssetType pageType, InputStream pageInput, AssetType thumbnailType, InputStream thumbnailInput);

  void createPage(String pageFilename, AssetMediaType pageMediaType, InputStream pageInput,
      String thumbnailFilename, AssetMediaType thumbnailMediaType, InputStream thumbnailInput);

  void createPage(String filename, String contentType, InputStream pageInput,
      String thumbnailFilename, String thumbnailContentType, InputStream thumbnailInput);

  void createAsset(String name, AssetType type, InputStream input);

  void createAsset(String name, String filename,AssetMediaType mediaType, InputStream input);

  void createAsset(String name, String filename, String contentType, InputStream input);

  void createProperty(String name, String value);

}