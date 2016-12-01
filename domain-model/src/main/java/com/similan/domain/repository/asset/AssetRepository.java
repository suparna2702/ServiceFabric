package com.similan.domain.repository.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.asset.Asset;
import com.similan.domain.entity.asset.AssetDetectionPolicy;
import com.similan.domain.entity.asset.AssetInfo;
import com.similan.domain.entity.asset.AssetMetadata;
import com.similan.domain.repository.asset.jpa.JpaAssetRepository;
import com.similan.service.api.asset.dto.basic.AssetMediaType;
import com.similan.service.api.asset.dto.basic.AssetType;

@Repository
public class AssetRepository {
  @Autowired
  private JpaAssetRepository repository;

  public Asset save(Asset entity) {
    return repository.save(entity);
  }

  public Asset findOne(Long id) {
    return repository.findOne(id);
  }

  public Asset create(
      AssetDetectionPolicy detectionPolicy, String category,
      String explicitFilename, AssetInfo providedInfo, String storeId,
      boolean attachment) {
    Asset asset = new Asset(detectionPolicy, category,
        explicitFilename, providedInfo, storeId, attachment);
    return asset;
  }

  public AssetInfo createInfo(String filename, AssetMediaType mediaType,
      AssetType type, AssetMetadata metadata) {
    AssetInfo info = new AssetInfo(filename, mediaType, type);
    info.setMetadata(metadata);
    return info;
  }

}
