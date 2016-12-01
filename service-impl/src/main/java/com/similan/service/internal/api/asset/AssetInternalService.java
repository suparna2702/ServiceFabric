package com.similan.service.internal.api.asset;

import java.io.IOException;

import com.similan.domain.entity.asset.Asset;
import com.similan.service.api.asset.AssetStream;
import com.similan.service.api.asset.NewAssetStream;
import com.similan.service.internal.api.asset.io.NewAsset;
import com.similan.service.internal.api.asset.io.TemporaryFile;
import com.similan.service.internal.impl.asset.AssetProcessingTask;

public interface AssetInternalService {
  Asset create(NewAsset newAsset, NewAssetStream assetStream,
      AssetProcessingTask... tasks);

  AssetStream retrieve(Asset asset);

  void delete(Asset asset);
  
  TemporaryFile createTemporaryFile() throws IOException;
}
