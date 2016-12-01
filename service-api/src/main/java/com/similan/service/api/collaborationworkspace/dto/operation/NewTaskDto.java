package com.similan.service.api.collaborationworkspace.dto.operation;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.collaborationworkspace.dto.basic.TaskType;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class NewTaskDto extends OperationDto {

  @XmlAttribute
  private String description;

  @XmlElement
  private SocialActorKey creator;

  @XmlElement
  private SocialActorKey assignee;

  @XmlAttribute
  private Date dueDate;

  @XmlAttribute
  private TaskType taskType;

  public NewTaskDto() {
  }

  public NewTaskDto(String description, String creatorName,
      String assigneeName, Date dueDate, TaskType taskType) {
    this(description, new SocialActorKey(creatorName), new SocialActorKey(
        assigneeName), dueDate, taskType);
  }

  public NewTaskDto(String description, SocialActorKey creator,
      SocialActorKey assignee, Date dueDate, TaskType taskType) {
    this.description = description;
    this.creator = creator;
    this.assignee = assignee;
    this.dueDate = dueDate;
    this.taskType = taskType;
  }

  public TaskType getTaskType() {
    return taskType;
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
