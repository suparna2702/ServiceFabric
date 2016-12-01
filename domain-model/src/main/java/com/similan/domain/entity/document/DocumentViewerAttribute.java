package com.similan.domain.entity.document;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.document.dto.basic.DocumentViewerItemType;

@Entity(name = "DocumentViewerAttribute")
@DiscriminatorValue("ATTRIBUTE")
public class DocumentViewerAttribute extends DocumentViewerItem {

  @Column(nullable = true)
  private String value;

  protected DocumentViewerAttribute() {
  }

  public DocumentViewerAttribute(String name, String value) {
    super(name, DocumentViewerItemType.ATTRIBUTE);
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
