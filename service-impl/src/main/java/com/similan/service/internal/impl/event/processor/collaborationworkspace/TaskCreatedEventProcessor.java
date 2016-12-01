package com.similan.service.internal.impl.event.processor.collaborationworkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.Task;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceWallEntry;
import com.similan.domain.repository.collaborationworkspace.TaskRepository;
import com.similan.service.internal.api.event.io.collaborationworkspace.TaskCreatedEvent;

@Component
public class TaskCreatedEventProcessor extends
    CollaborationWorkspaceEventProcessor<TaskCreatedEvent> {
  @Autowired
  private TaskRepository taskRepository;

  @Override
  protected CollaborationWorkspaceWallEntry createWallEntry(
      TaskCreatedEvent event) {
    long taskId = event.getTaskId();
    Task task = taskRepository.findOne(taskId);
    CollaborationWorkspace workspace = task.getWorkspace();
    Wall wall = getWall(workspace);
    return getCollaborationWorkspaceWallEntryRepository()
        .createTaskCreatedEntry(wall, task);
  }

}
