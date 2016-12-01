package com.similan.service.impl.document.extractor;

import java.io.IOException;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.impl.document.DerivedAssetFactory;
import com.similan.service.internal.api.asset.io.TemporaryFile;

public interface DocumentTextExtractor {
  void extract(AssetInfoDto info, TemporaryFile file,
      DerivedAssetFactory assetFactory) throws IOException;
}
