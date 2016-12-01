package com.similan.service.api.document.dto.basic;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import lombok.Getter;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.document.dto.key.DocumentCategoryKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.key.DocumentLabelKey;

public class DocumentDto extends KeyHolderDto<DocumentKey> {

  @XmlAttribute
  @Getter
  private String description;

  @XmlElement(name = "label")
  @XmlElementWrapper
  @Getter
  private List<DocumentLabelKey> labels;

  @XmlElement(name = "category")
  @XmlElementWrapper
  @Getter
  private List<DocumentCategoryKey> categories;

  protected DocumentDto() {
  }

  public DocumentDto(DocumentKey key, String description,
      List<DocumentLabelKey> labels, List<DocumentCategoryKey> categories) {
    super(key);
    this.description = description;
    this.labels = labels;
    this.categories = categories;
  }

}
