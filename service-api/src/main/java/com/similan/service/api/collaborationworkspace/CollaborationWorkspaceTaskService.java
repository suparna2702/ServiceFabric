package com.similan.service.api.collaborationworkspace;

import java.util.Date;
import java.util.List;

import com.similan.service.api.collaborationworkspace.dto.basic.TaskDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.TaskKey;
import com.similan.service.api.collaborationworkspace.dto.operation.NewTaskDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public interface CollaborationWorkspaceTaskService {

  TaskDto create(TaskKey taskKey, NewTaskDto newTask);

  List<TaskDto> getAllOpenTasks(CollaborationWorkspaceKey workspaceKey,
      SocialActorKey assigneeKey);

  List<TaskDto> getByAssigneeAndDate(CollaborationWorkspaceKey workspaceKey,
      SocialActorKey assigneeKey, Date date);

  List<TaskDto> getFutureByAssignee(CollaborationWorkspaceKey workspaceKey,
      SocialActorKey assigneeKey);

  List<TaskDto> getOverdueByAssignee(CollaborationWorkspaceKey workspaceKey,
      SocialActorKey assigneeKey);

  void cancelTask(TaskKey task);

  void completeTask(TaskKey task);

  TaskDto get(TaskKey taskKey);

}
