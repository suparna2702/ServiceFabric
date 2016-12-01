package com.similan.service.api.document.dto.extended;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.document.dto.basic.DocumentPageDto;
import com.similan.service.api.document.dto.key.DocumentInstanceKey;

public class DocumentInstanceAndDocumentAndViewerElementsDto extends
    DocumentInstanceAndDocumentDto {

  @XmlElement(name = "page")
  @XmlElementWrapper
  private List<DocumentPageDto> pages;

  @XmlElement(name = "item")
  @XmlElementWrapper
  private List<DocumentViewerItemDto> items;

  protected DocumentInstanceAndDocumentAndViewerElementsDto() {
  }

  public DocumentInstanceAndDocumentAndViewerElementsDto(
      DocumentInstanceKey key, AssetInfoDto detectedInfo, String viewerId,
      Boolean hasIcon, Boolean hasText, DocumentDto document,
      List<DocumentPageDto> pages, List<DocumentViewerItemDto> items) {
    super(key, detectedInfo, viewerId, hasIcon, hasText, document);
    this.pages = pages;
    this.items = items;
  }

  public List<DocumentPageDto> getPages() {
    return pages;
  }

  public List<DocumentViewerItemDto> getItems() {
    return items;
  }
}
