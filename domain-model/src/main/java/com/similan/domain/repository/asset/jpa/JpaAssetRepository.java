package com.similan.domain.repository.asset.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.asset.Asset;

public interface JpaAssetRepository extends JpaRepository<Asset, Long> {

}
