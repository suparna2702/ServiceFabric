package com.similan.service.api.document.dto.basic;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.document.dto.key.DocumentLabelKey;

public class DocumentLabelDto extends KeyHolderDto<DocumentLabelKey> {

  @XmlElement
  private DocumentLabelKey parent;

  protected DocumentLabelDto() {
  }

  public DocumentLabelDto(DocumentLabelKey key, DocumentLabelKey parent) {
    super(key);
    this.parent = parent;
  }

  public DocumentLabelKey getParent() {
    return parent;
  }
}
