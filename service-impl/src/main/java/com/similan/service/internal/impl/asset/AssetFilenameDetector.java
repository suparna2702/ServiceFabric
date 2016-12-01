package com.similan.service.internal.impl.asset;

import com.similan.service.api.asset.dto.basic.AssetMetadataDto;

public interface AssetFilenameDetector {

  String detect(String knownFilename, AssetMetadataDto metadata);

}
