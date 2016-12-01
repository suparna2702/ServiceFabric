package com.similan.service.internal.impl.asset.basic;

import org.springframework.stereotype.Component;

import com.similan.service.api.asset.dto.basic.AssetMediaType;
import com.similan.service.internal.impl.asset.AssetMediaTypeFinder;

@Component
public class DefaultAssetMediaTypeFinder implements AssetMediaTypeFinder {

  @Override
  public AssetMediaType find(String contentType) {
    return AssetMediaType.find(contentType);
  }

}
