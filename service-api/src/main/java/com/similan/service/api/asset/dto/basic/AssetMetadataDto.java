package com.similan.service.api.asset.dto.basic;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class AssetMetadataDto {

  @XmlAttribute
  private String contentType;
  @XmlAttribute
  private String contentDisposition;

  @XmlElement(name = "additional")
  @XmlElementWrapper
  private List<AssetAdditionalMetadataDto> additionals;

  protected AssetMetadataDto() {
  }

  public AssetMetadataDto(String contentType, String contentDisposition) {
    this(contentType, contentDisposition,
        new LinkedList<AssetAdditionalMetadataDto>());
  }

  public AssetMetadataDto(String contentType, String contentDisposition,
      List<AssetAdditionalMetadataDto> additionals) {
    this.contentType = contentType;
    this.contentDisposition = contentDisposition;
    this.additionals = additionals;
  }

  public String getContentType() {
    return contentType;
  }

  public String getContentDisposition() {
    return contentDisposition;
  }

  public List<AssetAdditionalMetadataDto> getAdditionals() {
    return additionals;
  }
}