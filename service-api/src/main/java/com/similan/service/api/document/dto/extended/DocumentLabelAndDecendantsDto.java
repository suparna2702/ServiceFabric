package com.similan.service.api.document.dto.extended;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.similan.service.api.document.dto.basic.DocumentLabelDto;
import com.similan.service.api.document.dto.key.DocumentLabelKey;

public class DocumentLabelAndDecendantsDto extends DocumentLabelDto {

  @XmlElement(name = "child")
  @XmlElementWrapper
  private List<DocumentLabelAndDecendantsDto> children;

  protected DocumentLabelAndDecendantsDto() {
  }

  public DocumentLabelAndDecendantsDto(DocumentLabelKey key,
      DocumentLabelKey parent, List<DocumentLabelAndDecendantsDto> children) {
    super(key, parent);
    this.children = children;
  }

  public List<DocumentLabelAndDecendantsDto> getChildren() {
    return children;
  }

}
