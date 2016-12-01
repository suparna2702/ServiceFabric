package com.similan.service.impl.document.viewer.image;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.Cleanup;

import org.springframework.stereotype.Component;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.asset.dto.basic.AssetType;
import com.similan.service.impl.document.util.ImageUtil;
import com.similan.service.impl.document.util.ImageUtil.Anchor;
import com.similan.service.impl.document.viewer.DocumentViewerComponentFactory;
import com.similan.service.internal.api.asset.io.TemporaryFile;

@Component
public class ImageAwtDocumentViewer extends AbstractImageDocumentViewer {
  private static final int MAX_WIDTH = 1024;
  private static final int MAX_HEIGHT = 1024;
  private static final AssetType TYPE = AssetType.PNG;

  public ImageAwtDocumentViewer() {
    super("image-awt");
  }

  @Override
  public void prepare(AssetInfoDto info, TemporaryFile file,
      DocumentViewerComponentFactory componentFactory) throws IOException {
    @Cleanup
    InputStream input = file.openInput();
    @Cleanup
    TemporaryFile resizedFile = componentFactory.createTemporaryFile();
    @Cleanup
    OutputStream resizedOutput = resizedFile.openOutput();
    ImageUtil.resize(input, Anchor.NONE, MAX_WIDTH, MAX_HEIGHT, resizedOutput,
        TYPE);
    resizedOutput.close();
    @Cleanup
    InputStream resizedInput = resizedFile.openInput();
    componentFactory.createPage(TYPE, resizedInput);
  }

}
