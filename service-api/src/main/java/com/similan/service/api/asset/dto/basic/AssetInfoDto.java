package com.similan.service.api.asset.dto.basic;

import javax.xml.bind.annotation.XmlAttribute;

public class AssetInfoDto {

  @XmlAttribute
  private String filename;

  @XmlAttribute
  private AssetMediaType mediaType;

  @XmlAttribute
  private AssetType type;

  @XmlAttribute
  private AssetMetadataDto metadata;

  protected AssetInfoDto() {
  }

  public AssetInfoDto(String filename, AssetMediaType mediaType,
      AssetType type, AssetMetadataDto metadata) {
    this.filename = filename;
    this.mediaType = mediaType;
    this.type = type;
    this.metadata = metadata;
  }

  public String getFilename() {
    return filename;
  }

  public AssetMediaType getMediaType() {
    return mediaType;
  }

  public AssetType getType() {
    return type;
  }

  public AssetMetadataDto getMetadata() {
    return metadata;
  }

}
