package com.similan.portal.model;

import java.io.Serializable;

import com.similan.service.api.document.dto.basic.DocumentCategoryDto;
import com.similan.service.api.document.dto.key.DocumentCategoryKey;

public class DocumentCategoryModel implements Serializable {

  private static final long serialVersionUID = -1613814314887775015L;
  private DocumentCategoryDto data;
  private boolean selected = false;
  private int count = 0;

  public DocumentCategoryModel(DocumentCategoryDto data) {
    this.data = data;
  }

  public DocumentCategoryModel(DocumentCategoryDto data, boolean selected) {
    this.data = data;
    this.selected = selected;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getName() {
    return data.getKey().getName();
  }

  public DocumentCategoryKey getKey() {
    return data.getKey();
  }

  public String toString() {
    return data.toString();
  }

  public int hashCode() {
    return data.hashCode();
  }

  public boolean equals(Object other) {
    return data.equals(other);
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public DocumentCategoryDto getData() {
    return data;
  }

}
