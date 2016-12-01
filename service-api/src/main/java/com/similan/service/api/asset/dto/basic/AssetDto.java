package com.similan.service.api.asset.dto.basic;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class AssetDto {

  @XmlAttribute
  private String explicitFilename;

  @XmlElement
  private AssetInfoDto providedInfo;

  @XmlElement
  private AssetInfoDto detectedInfo;

  protected AssetDto() {
  }

  public AssetDto(String explicitFilename, AssetInfoDto providedInfo,
      AssetInfoDto detectedInfo) {
    this.explicitFilename = explicitFilename;
    this.providedInfo = providedInfo;
    this.detectedInfo = detectedInfo;
  }

  public String getExplicitFilename() {
    return explicitFilename;
  }

  public AssetInfoDto getProvidedInfo() {
    return providedInfo;
  }

  public AssetInfoDto getDetectedInfo() {
    return detectedInfo;
  }

}
