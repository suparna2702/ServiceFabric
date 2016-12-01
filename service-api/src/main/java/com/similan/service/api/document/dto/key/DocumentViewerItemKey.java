package com.similan.service.api.document.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

public class DocumentViewerItemKey extends Key {

  @XmlElement
  private DocumentInstanceKey documentInstance;

  @XmlAttribute
  private String name;

  public DocumentViewerItemKey() {
  }

  public DocumentViewerItemKey(String workspaceOwnerName, String workspaceName,
      String documentName, Integer documentInstanceVersion, String name) {
    this(new DocumentInstanceKey(workspaceOwnerName, workspaceName,
        documentName, documentInstanceVersion), name);
  }

  public DocumentViewerItemKey(DocumentInstanceKey document, String name) {
    this.documentInstance = document;
    this.name = name;
  }

  public DocumentInstanceKey getDocumentInstance() {
    return documentInstance;
  }

  public String getName() {
    return name;
  }

  public EntityType getEntityType() {
    return EntityType.DOCUMENT_VIEWER_ITEM;
  }

}
