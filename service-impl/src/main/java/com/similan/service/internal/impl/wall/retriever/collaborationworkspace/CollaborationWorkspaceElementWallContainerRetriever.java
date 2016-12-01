package com.similan.service.internal.impl.wall.retriever.collaborationworkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceElement;
import com.similan.domain.entity.wall.Wall;
import com.similan.service.internal.api.wall.WallInternalService;
import com.similan.service.internal.impl.wall.WallRetriever;

@Component
public class CollaborationWorkspaceElementWallContainerRetriever implements
    WallRetriever<CollaborationWorkspaceElement> {

  @Autowired
  private WallInternalService wallInternalService;

  @Override
  public Wall getWall(CollaborationWorkspaceElement element) {
    CollaborationWorkspace workspace = element.getWorkspace();
    Wall wall = wallInternalService.get(workspace);
    return wall;
  }
}
