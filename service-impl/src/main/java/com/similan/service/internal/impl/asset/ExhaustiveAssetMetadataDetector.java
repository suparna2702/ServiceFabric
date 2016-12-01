package com.similan.service.internal.impl.asset;

import java.io.IOException;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.asset.dto.basic.AssetMetadataDto;
import com.similan.service.internal.api.asset.io.TemporaryFile;

public interface ExhaustiveAssetMetadataDetector {
  AssetMetadataDto detectMetadata(AssetInfoDto providedInfo,
      TemporaryFile file) throws IOException;
}
