package com.similan.service.internal.impl.asset;

import java.io.IOException;

import com.similan.domain.entity.asset.Asset;
import com.similan.service.internal.api.asset.io.TemporaryFile;

public interface AssetProcessingTask {
  void process(Asset asset, TemporaryFile file) throws IOException;
}
