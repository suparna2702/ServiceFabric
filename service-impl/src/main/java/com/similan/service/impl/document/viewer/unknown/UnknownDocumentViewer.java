package com.similan.service.impl.document.viewer.unknown;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.impl.document.viewer.AbstractDocumentViewer;
import com.similan.service.impl.document.viewer.DocumentViewerComponentFactory;
import com.similan.service.internal.api.asset.io.TemporaryFile;

@Component
public class UnknownDocumentViewer extends AbstractDocumentViewer {
  public UnknownDocumentViewer() {
    super("unknown");
  }

  @Override
  public void prepare(AssetInfoDto info, TemporaryFile file,
      DocumentViewerComponentFactory componentFactory) throws IOException {
  }
}
