package com.similan.service.api.document.dto.operation;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.document.dto.key.DocumentLabelKey;

public class NewDocumentLabelDto extends OperationDto {

  @XmlElement
  private DocumentLabelKey parent;

  public NewDocumentLabelDto() {
  }

  public NewDocumentLabelDto(String parentOwnerName, String parentName) {
    this(new DocumentLabelKey(parentOwnerName, parentName));
  }

  public NewDocumentLabelDto(DocumentLabelKey parent) {
    this.parent = parent;
  }

  public DocumentLabelKey getParent() {
    return parent;
  }
}
