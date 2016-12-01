package com.similan.service.api.collaborationworkspace.dto.basic;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.key.TaskKey;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class TaskDto extends KeyHolderDto<TaskKey> {

  @XmlAttribute
  private Date creationDate;

  @XmlAttribute
  private String description;

  @XmlElement
  private SocialActorKey creator;

  @XmlElement
  private SocialActorKey assignee;

  @XmlAttribute
  private Date dueDate;

  @XmlAttribute
  private TaskType type;

  @XmlAttribute
  private TaskStatus status;

  protected TaskDto() {
  }

  public TaskDto(TaskKey key, Date creationDate, String description,
      SocialActorKey creator, SocialActorKey assignee, Date dueDate,
      TaskType type, TaskStatus status) {
    super(key);
    this.creationDate = creationDate;
    this.description = description;
    this.creator = creator;
    this.assignee = assignee;
    this.dueDate = dueDate;
    this.type = type;
    this.status = status;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public TaskType getType() {
    return type;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public String getDescription() {
    return description;
  }

  public SocialActorKey getCreator() {
    return creator;
  }

  public SocialActorKey getAssignee() {
    return assignee;
  }

  public Date getDueDate() {
    return dueDate;
  }

}
