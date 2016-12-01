package com.similan.domain.repository.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.asset.AssetMetadata;
import com.similan.domain.repository.asset.jpa.JpaAssetMetadataRepository;

@Repository
public class AssetMetadataRepository {
  @Autowired
  private JpaAssetMetadataRepository repository;

  public AssetMetadata save(AssetMetadata entity) {
    return repository.save(entity);
  }

  public AssetMetadata findOne(Long id) {
    return repository.findOne(id);
  }

  public AssetMetadata create(String contentType, String contentDisposition) {
    AssetMetadata metadata = new AssetMetadata(contentType,
            contentDisposition);
    return metadata;
  }
}
