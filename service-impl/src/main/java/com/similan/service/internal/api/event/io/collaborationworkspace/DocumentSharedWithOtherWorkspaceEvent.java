package com.similan.service.internal.api.event.io.collaborationworkspace;

public class DocumentSharedWithOtherWorkspaceEvent extends
    CollaborationWorkspaceEvent {

  private static final long serialVersionUID = 1L;

  private long sharedDocumentId;

  private long workspaceFromId;

  public DocumentSharedWithOtherWorkspaceEvent(long sharedDocumentId,
      long workspaceFromId) {
    this.sharedDocumentId = sharedDocumentId;
    this.workspaceFromId = workspaceFromId;
  }

  public long getSharedDocumentId() {
    return sharedDocumentId;
  }

  public long getWorkspaceFromId() {
    return workspaceFromId;
  }

}
