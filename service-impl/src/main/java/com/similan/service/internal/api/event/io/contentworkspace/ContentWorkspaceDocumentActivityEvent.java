package com.similan.service.internal.api.event.io.contentworkspace;

import com.similan.service.api.wall.dto.basic.WallEntryType;

public class ContentWorkspaceDocumentActivityEvent extends
    ContentWorkspaceEvent {

  private static final long serialVersionUID = 1L;

  private Long documentId;

  private Long initiatorId;

  private Long commentId;

  public ContentWorkspaceDocumentActivityEvent(Long documentId,
      Long initiatorId, Long contentWorkspaceId, WallEntryType entryType) {
    super(contentWorkspaceId, entryType);
    this.documentId = documentId;
    this.initiatorId = initiatorId;
  }

  public ContentWorkspaceDocumentActivityEvent(Long documentId,
      Long initiatorId, Long contentWorkspaceId, WallEntryType entryType,
      Long commentId) {
    this(documentId, initiatorId, contentWorkspaceId, entryType);
    this.commentId = commentId;
  }

  public Long getCommentId() {
    return commentId;
  }

  public Long getDocumentId() {
    return documentId;
  }

  public Long getInitiatorId() {
    return initiatorId;
  }

}
