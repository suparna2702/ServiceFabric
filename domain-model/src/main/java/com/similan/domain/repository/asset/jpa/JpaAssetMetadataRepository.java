package com.similan.domain.repository.asset.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.asset.AssetMetadata;

public interface JpaAssetMetadataRepository extends
    JpaRepository<AssetMetadata, Long> {

}
