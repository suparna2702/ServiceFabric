package com.similan.service.impl.document.icon.awt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.Cleanup;

import org.springframework.stereotype.Component;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.asset.dto.basic.AssetType;
import com.similan.service.impl.document.DerivedAssetFactory;
import com.similan.service.impl.document.icon.DocumentIconGenerator;
import com.similan.service.impl.document.util.ImageUtil;
import com.similan.service.impl.document.util.ImageUtil.Anchor;
import com.similan.service.internal.api.asset.io.TemporaryFile;

@Component
public class AwtIconExtractor implements DocumentIconGenerator {
  public static final AssetType TYPE = AssetType.PNG;

  @Override
  public void generate(AssetInfoDto info, TemporaryFile file,
      DerivedAssetFactory assetFactory) throws IOException {
    @Cleanup
    InputStream input = file.openInput();
    @Cleanup
    TemporaryFile iconFile = assetFactory.createTemporaryFile();
    @Cleanup
    OutputStream iconOutput = iconFile.openOutput();
    ImageUtil.resize(input, Anchor.LARGEST, SIZE, SIZE, iconOutput, TYPE);
    iconOutput.close();
    @Cleanup
    InputStream iconInput = iconFile.openInput();
    assetFactory.create(iconInput);
  }
}
