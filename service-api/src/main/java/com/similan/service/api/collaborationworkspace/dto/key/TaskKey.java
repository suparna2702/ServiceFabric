package com.similan.service.api.collaborationworkspace.dto.key;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.comment.dto.key.ICommentableKey;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;

public class TaskKey extends Key implements ICommentableKey, IWallEntrySubjectKey {

  private CollaborationWorkspaceKey workspace;

  private String name;

  public TaskKey(String workspaceOwnerName, String workspaceName, String name) {
    this(new CollaborationWorkspaceKey(workspaceOwnerName, workspaceName), name);
  }

  public TaskKey(CollaborationWorkspaceKey workspace, String name) {
    this.workspace = workspace;
    this.name = name;
  }

  public CollaborationWorkspaceKey getWorkspace() {
    return workspace;
  }

  public String getName() {
    return name;
  }

  public EntityType getEntityType() {
    return EntityType.TASK;
  }

}
