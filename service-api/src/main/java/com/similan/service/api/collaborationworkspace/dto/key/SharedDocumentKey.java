package com.similan.service.api.collaborationworkspace.dto.key;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.bookmark.dto.key.IBookmarkableKey;
import com.similan.service.api.comment.dto.key.ICommentableKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;

public class SharedDocumentKey extends Key implements ICommentableKey,
    IBookmarkableKey, IWallEntrySubjectKey {

  @XmlElement
  private CollaborationWorkspaceKey workspace;

  @XmlElement
  private DocumentKey document;

  public SharedDocumentKey() {
  }

  public SharedDocumentKey(String workspaceOwnerName, String workspaceName,
      String documentWorkspaceOwnerName, String documentWorkspaceName,
      String documentName) {
    this(new CollaborationWorkspaceKey(workspaceOwnerName, workspaceName),
        new DocumentKey(documentWorkspaceOwnerName, documentWorkspaceName,
            documentName));
  }

  public SharedDocumentKey(CollaborationWorkspaceKey workspace,
      DocumentKey document) {
    this.document = document;
    this.workspace = workspace;
  }

  public CollaborationWorkspaceKey getWorkspace() {
    return workspace;
  }

  public DocumentKey getDocument() {
    return document;
  }

  public EntityType getEntityType() {
    return EntityType.SHARED_DOCUMENT;
  }

}
