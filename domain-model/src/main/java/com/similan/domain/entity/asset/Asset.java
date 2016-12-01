package com.similan.domain.entity.asset;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Asset")
public class Asset {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(nullable = true)
  private String explicitFilename;

  @Column(nullable = false)
  private long size;

  private AssetInfo providedInfo;

  private AssetInfo detectedInfo;

  @Column(nullable = true)
  private String storeId;

  @Column(nullable = true)
  private String category;

  @Column(nullable = true)
  private String storeKey;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AssetDetectionPolicy detectionPolicy;
  
  @Column(nullable = false)
  private boolean attachment;

  protected Asset() {
  }

  public Asset(
      AssetDetectionPolicy detectionPolicy, String category,
      String explicitFilename, AssetInfo providedInfo, String storeId,
      boolean attachment) {
    this.detectionPolicy = detectionPolicy;
    this.category = category;
    this.explicitFilename = explicitFilename;
    this.storeId = storeId;
    this.providedInfo = providedInfo;
    this.attachment = attachment;
  }
}
