package com.similan.service.api.asset.dto.basic;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.basic.IDto;

public class AssetAdditionalMetadataDto implements IDto {

  @XmlAttribute
  private String name;

  @XmlAttribute
  private String value;

  protected AssetAdditionalMetadataDto() {
  }

  public AssetAdditionalMetadataDto(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }
}