package com.similan.service.internal.impl.asset;

import com.similan.service.api.asset.dto.basic.AssetMediaType;

public interface AssetMediaTypeFinder {

  AssetMediaType find(String contentType);

}
