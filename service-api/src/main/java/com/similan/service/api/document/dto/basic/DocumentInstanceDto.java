package com.similan.service.api.document.dto.basic;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.document.dto.key.DocumentInstanceKey;

public class DocumentInstanceDto extends KeyHolderDto<DocumentInstanceKey> {

  @XmlElement
  private AssetInfoDto detectedInfo;

  @XmlAttribute
  private Boolean hasIcon;

  @XmlAttribute
  private Boolean hasText;

  @XmlAttribute
  private String viewerId;

  protected DocumentInstanceDto() {
  }

  public DocumentInstanceDto(DocumentInstanceKey key,
      AssetInfoDto detectedInfo, String viewerId, Boolean hasIcon,
      Boolean hasText) {
    super(key);
    this.detectedInfo = detectedInfo;
    this.viewerId = viewerId;
    this.hasIcon = hasIcon;
    this.hasText = hasText;
  }

  public AssetInfoDto getDetectedInfo() {
    return detectedInfo;
  }

  public String getViewerId() {
    return viewerId;
  }

  public Boolean getHasIcon() {
    return hasIcon;
  }

  public Boolean getHasText() {
    return hasText;
  }

}
