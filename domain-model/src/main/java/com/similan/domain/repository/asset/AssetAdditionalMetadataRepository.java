package com.similan.domain.repository.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.asset.AssetAdditionalMetadata;
import com.similan.domain.entity.asset.AssetMetadata;
import com.similan.domain.repository.asset.jpa.JpaAssetAdditionalMetadataRepository;

@Repository
public class AssetAdditionalMetadataRepository {
  @Autowired
  private JpaAssetAdditionalMetadataRepository repository;

  public AssetAdditionalMetadata save(AssetAdditionalMetadata entity) {
    return repository.save(entity);
  }

  public AssetAdditionalMetadata findOne(Long id) {
    return repository.findOne(id);
  }

  public AssetAdditionalMetadata create(AssetMetadata metadata, String name,
      String value) {
    AssetAdditionalMetadata additional = new AssetAdditionalMetadata(name,
            value);
    additional.setMetadata(metadata);
    return additional;
  }

}
