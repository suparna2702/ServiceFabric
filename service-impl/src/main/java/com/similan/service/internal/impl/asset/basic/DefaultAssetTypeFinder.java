package com.similan.service.internal.impl.asset.basic;

import org.springframework.stereotype.Component;

import com.similan.service.api.asset.dto.basic.AssetMediaType;
import com.similan.service.api.asset.dto.basic.AssetType;
import com.similan.service.internal.impl.asset.AssetTypeFinder;

@Component
public class DefaultAssetTypeFinder implements AssetTypeFinder {

  @Override
  public AssetType find(AssetMediaType mediaType, String filename) {
    return AssetType.find(mediaType, filename);
  }

}
