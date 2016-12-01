package com.similan.domain.entity.asset;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.similan.service.api.asset.dto.basic.AssetMediaType;
import com.similan.service.api.asset.dto.basic.AssetType;

@Embeddable
public class AssetInfo {

  @Column(nullable = true)
  private String filename;

  @Enumerated(EnumType.STRING)
  @Column(nullable = true)
  private AssetMediaType mediaType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = true)
  private AssetType type;

  @OneToOne(optional = true)
  @JoinColumn(nullable = true)
  private AssetMetadata metadata;

  protected AssetInfo() {
  }

  public AssetInfo(String filename, AssetMediaType mediaType, AssetType type) {
    this.filename = filename;
    this.mediaType = mediaType;
    this.type = type;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public AssetMediaType getMediaType() {
    return mediaType;
  }

  public void setMediaType(AssetMediaType mediaType) {
    this.mediaType = mediaType;
  }

  public AssetType getType() {
    return type;
  }

  public void setType(AssetType type) {
    this.type = type;
  }

  public AssetMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(AssetMetadata metadata) {
    this.metadata = metadata;
  }

}
