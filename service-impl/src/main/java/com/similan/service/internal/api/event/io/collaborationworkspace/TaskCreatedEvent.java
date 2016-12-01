package com.similan.service.internal.api.event.io.collaborationworkspace;

public class TaskCreatedEvent extends CollaborationWorkspaceEvent {

  private static final long serialVersionUID = 1L;

  private long taskId;

  public TaskCreatedEvent(long taskId) {
    this.taskId = taskId;
  }

  public long getTaskId() {
    return taskId;
  }

}
