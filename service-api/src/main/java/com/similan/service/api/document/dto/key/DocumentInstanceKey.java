package com.similan.service.api.document.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.comment.dto.key.ICommentableKey;

public class DocumentInstanceKey extends Key implements ICommentableKey {

  @XmlElement
  private DocumentKey document;

  @XmlAttribute
  private Integer version;

  public DocumentInstanceKey() {
  }

  public DocumentInstanceKey(String workspaceOwnerName, String workspaceName,
      String documentName, Integer version) {
    this(new DocumentKey(workspaceOwnerName, workspaceName, documentName),
        version);
  }

  public DocumentInstanceKey(DocumentKey document, Integer version) {
    this.document = document;
    this.version = version;
  }

  public DocumentKey getDocument() {
    return document;
  }

  public Integer getVersion() {
    return version;
  }

  public EntityType getEntityType() {
    return EntityType.DOCUMENT_INSTANCE;
  }

}
