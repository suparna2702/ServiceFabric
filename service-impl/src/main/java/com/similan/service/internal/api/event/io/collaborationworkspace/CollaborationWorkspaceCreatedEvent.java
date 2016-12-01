package com.similan.service.internal.api.event.io.collaborationworkspace;

public class CollaborationWorkspaceCreatedEvent extends
    CollaborationWorkspaceEvent {

  private static final long serialVersionUID = 1L;

  private Long workspaceId;

  public CollaborationWorkspaceCreatedEvent(Long workspaceId) {
    this.workspaceId = workspaceId;
  }

  public Long getWorkspaceId() {
    return workspaceId;
  }

}
