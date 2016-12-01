package com.similan.service.api.document.dto.extended;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.document.dto.basic.DocumentInstanceDto;
import com.similan.service.api.document.dto.key.DocumentInstanceKey;

public class DocumentInstanceAndDocumentDto extends DocumentInstanceDto {

  @XmlElement
  private DocumentDto document;

  protected DocumentInstanceAndDocumentDto() {
  }

  public DocumentInstanceAndDocumentDto(DocumentInstanceKey key,
      AssetInfoDto detectedInfo, String viewerId, Boolean hasIcon,
      Boolean hasText, DocumentDto document) {
    super(key, detectedInfo, viewerId, hasIcon, hasText);
    this.document = document;
  }

  public DocumentDto getDocument() {
    return document;
  }

}
