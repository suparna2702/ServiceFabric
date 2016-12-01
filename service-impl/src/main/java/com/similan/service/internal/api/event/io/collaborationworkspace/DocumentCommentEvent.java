package com.similan.service.internal.api.event.io.collaborationworkspace;

public class DocumentCommentEvent extends CollaborationWorkspaceEvent {

  private static final long serialVersionUID = 1L;

  private long documentId;

  private long initiatorId;

  private long commentId;

  public DocumentCommentEvent(long documentId, long initiatorId, long commentId) {
    this.commentId = commentId;
    this.documentId = documentId;
    this.initiatorId = initiatorId;
  }

  public long getDocumentId() {
    return documentId;
  }

  public long getInitiatorId() {
    return initiatorId;
  }

  public long getCommentId() {
    return commentId;
  }

}
