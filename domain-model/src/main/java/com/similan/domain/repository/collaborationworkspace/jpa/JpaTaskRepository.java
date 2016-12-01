package com.similan.domain.repository.collaborationworkspace.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.Task;
import com.similan.domain.entity.community.SocialActor;
import com.similan.service.api.collaborationworkspace.dto.basic.TaskStatus;
import com.similan.service.api.collaborationworkspace.dto.basic.TaskType;

public interface JpaTaskRepository extends JpaRepository<Task, Long> {

  @Query("select task from Task task"
      + " where task.workspace.owner.name=:workspaceOwnerName"
      + " and task.workspace.name=:workspaceName" + " and task.name=:name")
  Task findOne(@Param("workspaceOwnerName") String workspaceOwnerName,
      @Param("workspaceName") String workspaceName, @Param("name") String name);

  @Query("select task from Task task"
      + " where task.workspace =:workspace and task.type=:type"
      + " order by task.name")
  List<Task> findByWorkspaceAndType(
      @Param("workspace") CollaborationWorkspace workspace,
      @Param("type") TaskType type);

  @Query("select task from Task task"
      + " where task.workspace =:workspace and task.assignee =:assignee"
      + " order by task.name")
  List<Task> findByWorkspaceAndAssignee(
      @Param("workspace") CollaborationWorkspace workspace,
      @Param("assignee") SocialActor assignee);

  @Query("select task from Task task"
      + " where task.workspace =:workspace and " + "task.assignee =:assignee "
      + "and task.status =:status")
  List<Task> findByWorkspaceAndAssigneeAndStatus(
      @Param("workspace") CollaborationWorkspace workspace,
      @Param("assignee") SocialActor assignee,
      @Param("status") TaskStatus status);

}
