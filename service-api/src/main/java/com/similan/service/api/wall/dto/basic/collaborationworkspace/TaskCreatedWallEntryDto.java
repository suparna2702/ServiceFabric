package com.similan.service.api.wall.dto.basic.collaborationworkspace;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import lombok.ToString;

import com.similan.service.api.collaborationworkspace.dto.basic.TaskDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.WallEntryKey;

@ToString
public class TaskCreatedWallEntryDto extends CollaborationWorkspaceWallEntryDto {

  @XmlElement
  private TaskDto task;

  protected TaskCreatedWallEntryDto() {
  }

  public TaskCreatedWallEntryDto(WallEntryKey<CollaborationWorkspaceKey> key,
      ActorDto initiator, Date date, TaskDto task) {
    super(key, initiator, date);
    this.task = task;
  }

  public TaskDto getTask() {
    return task;
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.COLLABORATION_WORKSPACE_TASK_CREATED;
  }

}
