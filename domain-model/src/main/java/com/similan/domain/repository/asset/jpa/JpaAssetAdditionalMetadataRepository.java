package com.similan.domain.repository.asset.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.asset.AssetAdditionalMetadata;

public interface JpaAssetAdditionalMetadataRepository extends
    JpaRepository<AssetAdditionalMetadata, Long> {

}
