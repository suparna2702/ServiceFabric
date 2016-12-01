package com.similan.service.api.document.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.comment.dto.key.ICommentableKey;

public class DocumentPageKey extends Key implements ICommentableKey {

  @XmlElement
  private DocumentInstanceKey documentInstance;

  @XmlAttribute
  private Integer number;

  public DocumentPageKey() {
  }

  public DocumentPageKey(String workspaceOwnerName, String workspaceName,
      String documentName, Integer documentInstanceVersion, Integer number) {
    this(new DocumentInstanceKey(workspaceOwnerName, workspaceName,
        documentName, documentInstanceVersion), number);
  }

  public DocumentPageKey(DocumentInstanceKey documentInstance, Integer number) {
    this.documentInstance = documentInstance;
    this.number = number;
  }

  public DocumentInstanceKey getDocumentInstance() {
    return documentInstance;
  }

  public Integer getNumber() {
    return number;
  }

  public EntityType getEntityType() {
    return EntityType.DOCUMENT_PAGE;
  }

}
