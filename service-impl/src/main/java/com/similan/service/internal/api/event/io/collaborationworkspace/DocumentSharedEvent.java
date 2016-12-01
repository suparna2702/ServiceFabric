package com.similan.service.internal.api.event.io.collaborationworkspace;

public class DocumentSharedEvent extends CollaborationWorkspaceEvent {

  private static final long serialVersionUID = 1L;

  private long sharedDocumentId;

  public DocumentSharedEvent(long sharedDocumentId) {
    this.sharedDocumentId = sharedDocumentId;
  }

  public long getSharedDocumentId() {
    return sharedDocumentId;
  }

}
