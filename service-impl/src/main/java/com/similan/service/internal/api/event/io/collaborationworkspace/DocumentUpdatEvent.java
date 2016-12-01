package com.similan.service.internal.api.event.io.collaborationworkspace;

public class DocumentUpdatEvent extends CollaborationWorkspaceEvent {

  private static final long serialVersionUID = 1L;

  private Long sharedDocument;

  private Long updater;

  public DocumentUpdatEvent(Long sharedDocument, Long updater) {
    this.sharedDocument = sharedDocument;
    this.updater = updater;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public Long getSharedDocument() {
    return sharedDocument;
  }

  public Long getUpdater() {
    return updater;
  }

  @Override
  public String toString() {
    return "DocumentUpdatedEvent [sharedDocument=" + sharedDocument
        + ", updater=" + updater + "]";
  }

}
