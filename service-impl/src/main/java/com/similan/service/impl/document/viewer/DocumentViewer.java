package com.similan.service.impl.document.viewer;

import java.io.IOException;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.internal.api.asset.io.TemporaryFile;

public interface DocumentViewer {

  String getId();

  void prepare(AssetInfoDto info, TemporaryFile file,
      DocumentViewerComponentFactory componentFactory) throws IOException;

}
