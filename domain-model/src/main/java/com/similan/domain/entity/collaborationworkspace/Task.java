package com.similan.domain.entity.collaborationworkspace;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.comment.ICommentable;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.collaborationworkspace.dto.basic.TaskStatus;
import com.similan.service.api.collaborationworkspace.dto.basic.TaskType;

@Entity(name = "Task")
@DiscriminatorValue("Task")
public class Task extends CollaborationWorkspaceElement implements
    ICommentable, IWallEntrySubject {

  @Column(nullable = false)
  private String name;

  @Column(nullable = true, length = 2000)
  private String description;

  @Column(nullable = true)
  private Date dueDate;

  @ManyToOne(optional = true)
  @JoinColumn(nullable = true)
  private SocialActor assignee;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TaskType type;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TaskStatus status;

  protected Task() {
  }

  public Task(Date creationDate, TaskType type, String name,
      String description, Date dueDate, TaskStatus status) {
    super(creationDate);
    this.type = type;
    this.name = name;
    this.description = description;
    this.dueDate = dueDate;
    this.status = status;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  public TaskType getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public SocialActor getAssignee() {
    return assignee;
  }

  public void setAssignee(SocialActor assignee) {
    this.assignee = assignee;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.TASK;
  }

}
