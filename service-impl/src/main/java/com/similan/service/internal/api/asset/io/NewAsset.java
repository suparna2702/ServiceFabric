package com.similan.service.internal.api.asset.io;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import com.similan.domain.entity.asset.AssetDetectionPolicy;
import com.similan.service.api.asset.dto.basic.AssetMetadataDto;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewAsset {
  AssetDetectionPolicy detectionPolicy;
  String category;
  String explicitFilename;
  AssetMetadataDto providedMetadata;
  Boolean attachment;
}
