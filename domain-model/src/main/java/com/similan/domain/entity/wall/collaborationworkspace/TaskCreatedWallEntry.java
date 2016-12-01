package com.similan.domain.entity.wall.collaborationworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "TaskCreatedWallEntry")
@DiscriminatorValue("COLLABORATION_WORKSPACE_TASK_CREATED")
public class TaskCreatedWallEntry extends CollaborationWorkspaceWallEntry {

  public TaskCreatedWallEntry() {
  }

  public TaskCreatedWallEntry(int number, Date date) {
    super(WallEntryType.COLLABORATION_WORKSPACE_TASK_CREATED, number, date);
  }
}
