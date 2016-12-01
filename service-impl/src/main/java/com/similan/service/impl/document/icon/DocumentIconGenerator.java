package com.similan.service.impl.document.icon;

import java.io.IOException;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.impl.document.DerivedAssetFactory;
import com.similan.service.internal.api.asset.io.TemporaryFile;

public interface DocumentIconGenerator {
  public static final int SIZE = 64;

  void generate(AssetInfoDto info, TemporaryFile file,
      DerivedAssetFactory assetFactory) throws IOException;
}
