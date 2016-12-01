package com.similan.domain.repository.collaborationworkspace;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.Task;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.repository.collaborationworkspace.jpa.JpaTaskRepository;
import com.similan.service.api.collaborationworkspace.dto.basic.TaskStatus;
import com.similan.service.api.collaborationworkspace.dto.basic.TaskType;

@Repository
public class TaskRepository {
  @Autowired
  private JpaTaskRepository repository;

  public List<Task> findAll() {
    return repository.findAll();
  }

  public Task save(Task task) {
    return repository.save(task);
  }

  public Task findOne(Long id) {
    return repository.findOne(id);
  }

  public Task findOne(String workspaceOwnerName, String workspaceName,
      String name) {
    return repository.findOne(workspaceOwnerName, workspaceName,
      name);
  }

  public List<Task> findByWorkspaceAndType(
      CollaborationWorkspace workspace, TaskType taskType) {
    return repository.findByWorkspaceAndType(
      workspace, taskType);
  }

  public List<Task> findByWorkspaceAndAssignee(
      CollaborationWorkspace workspace, SocialActor assignee) {
    return repository.findByWorkspaceAndAssignee(
      workspace, assignee);
  }

  public List<Task> findByWorkspaceAndAssigneeAndStatus(
      CollaborationWorkspace workspace, SocialActor assignee,
      TaskStatus taskStatus) {
    return repository.findByWorkspaceAndAssigneeAndStatus(
      workspace, assignee,
      taskStatus);
  }

  public Task create(CollaborationWorkspace workspace, SocialActor initiator,
      Date creationDate, TaskType type, String name, String description,
      Date dueDate, TaskStatus status, SocialActor assignee) {
    Task task = new Task(creationDate, type, name, description, dueDate, status);
    task.setWorkspace(workspace);
    task.setCreator(initiator);
    task.setAssignee(assignee);
    return task;
  }

}
