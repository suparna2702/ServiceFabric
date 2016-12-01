package com.similan.service.api.document.dto.extended;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.document.dto.basic.DocumentViewerItemType;
import com.similan.service.api.document.dto.key.DocumentViewerItemKey;

public class DocumentViewerItemDto extends KeyHolderDto<DocumentViewerItemKey> {

  @XmlAttribute
  private DocumentViewerItemType type;

  @XmlAttribute
  private String value;

  protected DocumentViewerItemDto() {
  }

  public DocumentViewerItemDto(DocumentViewerItemKey key,
      DocumentViewerItemType type, String value) {
    super(key);
    this.type = type;
    this.value = value;
  }

  public DocumentViewerItemType getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

}
