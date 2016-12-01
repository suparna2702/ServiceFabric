package com.similan.service.internal.impl.asset;

import com.similan.service.api.asset.dto.basic.AssetMediaType;
import com.similan.service.api.asset.dto.basic.AssetType;

public interface AssetTypeFinder {

  AssetType find(AssetMediaType mediaType, String filename);

}
