package com.similan.portal.model;

import java.io.Serializable;
import java.net.URLEncoder;

import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.extended.DocumentStatisticsDto;
import com.similan.service.api.document.dto.key.DocumentInstanceKey;

public class DocumentInstanceAndDocumentModel implements Serializable {

  private static final long serialVersionUID = -1613814314887775015L;
  private DocumentInstanceAndDocumentDto data;
  private boolean selected = false;
  private boolean checkedOut = false;
  private boolean checkedOutByUser = false;
  private String documentStorageKey;
  private DocumentStatisticsDto statistics;

  public DocumentInstanceAndDocumentModel(DocumentInstanceAndDocumentDto data) {
    this.data = data;
  }

  public DocumentInstanceAndDocumentModel(DocumentInstanceAndDocumentDto data,
      boolean selected) {
    this.data = data;
    this.selected = selected;
  }

  public String getName() {
    return data.getKey().getDocument().getName();
  }

  public DocumentInstanceKey getKey() {
    return data.getKey();
  }

  @Override
  public String toString() {
    return "DocumentInstanceAndDocumentModel [data=" + data + ", selected="
        + selected + ", checkedOut=" + checkedOut + ", checkedOutByUser="
        + checkedOutByUser + ", documentStorageKey=" + documentStorageKey
        + ", statistics=" + statistics + "]";
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

  public DocumentInstanceAndDocumentDto getData() {
    return data;
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

  public DocumentStatisticsDto getStatistics() {
    return statistics;
  }

  public void setStatistics(DocumentStatisticsDto statistics) {
    this.statistics = statistics;
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
