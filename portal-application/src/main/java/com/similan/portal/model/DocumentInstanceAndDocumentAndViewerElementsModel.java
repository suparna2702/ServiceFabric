package com.similan.portal.model;

import java.io.Serializable;
import java.net.URLEncoder;

import lombok.ToString;

import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.document.dto.key.DocumentInstanceKey;

@ToString
public class DocumentInstanceAndDocumentAndViewerElementsModel implements
    Serializable {

  private static final long serialVersionUID = -1613814314887775015L;
  private DocumentInstanceAndDocumentAndViewerElementsDto data;
  private boolean selected = false;
  private boolean checkedOut = false;
  private boolean checkedOutByUser = false;
  private String documentStorageKey;

  public DocumentInstanceAndDocumentAndViewerElementsModel(
      DocumentInstanceAndDocumentAndViewerElementsDto data) {
    this.data = data;
  }

  public DocumentInstanceAndDocumentAndViewerElementsModel(
      DocumentInstanceAndDocumentAndViewerElementsDto data, boolean selected) {
    this.data = data;
    this.selected = selected;
  }

  public String getName() {
    return data.getKey().getDocument().getName();
  }

  public DocumentInstanceKey getKey() {
    return data.getKey();
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

  public DocumentInstanceAndDocumentAndViewerElementsDto getData() {
    return data;
  }

  public void setData(DocumentInstanceAndDocumentAndViewerElementsDto data) {
    this.data = data;
  }

  public boolean isCheckedOut() {
    return checkedOut;
  }

  public void setCheckedOut(boolean checkedOut) {
    this.checkedOut = checkedOut;
  }

  public boolean isCheckedOutByUser() {
    return checkedOutByUser;
  }

  public void setCheckedOutByUser(boolean checkedOutByUser) {
    this.checkedOutByUser = checkedOutByUser;
  }

  public String getPageStorageKey() {
    // {workspaceOwnerName}/{workspaceName}/{documentName}/{documentInstanceVersion}/{number}",
    // method = RequestMethod.GET
    if (documentStorageKey == null) {
      try {
        StringBuilder builder = new StringBuilder();
        builder
            .append(
                data.getKey().getDocument().getWorkspace().getOwner().getName())
            .append('/')
            .append(
                URLEncoder.encode(data.getKey().getDocument().getWorkspace()
                    .getName(), "ISO-8859-1"))
            .append('/')
            .append(
                URLEncoder.encode(data.getKey().getDocument().getName(),
                    "ISO-8859-1")).append('/')
            .append(data.getKey().getVersion());

        documentStorageKey = builder.toString();
      } catch (Exception e) {
        documentStorageKey = "";
      }
    }
    return documentStorageKey;
  }

}
