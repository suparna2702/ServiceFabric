package com.similan.service.internal.api.event.io.collaborationworkspace;

public class DocumentDownloadedEvent extends CollaborationWorkspaceEvent {

  private static final long serialVersionUID = 1L;

  private long sharedDocumentId;

  private long downloaderId;

  public DocumentDownloadedEvent(long sharedDocumentId, long downloaderId) {
    this.sharedDocumentId = sharedDocumentId;
    this.downloaderId = downloaderId;

  }

  public long getSharedDocumentId() {
    return sharedDocumentId;
  }

  public long getDownloaderId() {
    return downloaderId;
  }

}
