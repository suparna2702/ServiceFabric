package com.similan.service.api.document.dto.basic;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.document.dto.key.DocumentPageKey;

public class DocumentPageDto extends KeyHolderDto<DocumentPageKey> {

  @XmlAttribute
  private Boolean hasThumbnail;

  protected DocumentPageDto() {
  }

  public DocumentPageDto(DocumentPageKey key, Boolean hasThumbnail) {
    super(key);
    this.hasThumbnail = hasThumbnail;
  }

  public Boolean getHasThumbnail() {
    return hasThumbnail;
  }
}
