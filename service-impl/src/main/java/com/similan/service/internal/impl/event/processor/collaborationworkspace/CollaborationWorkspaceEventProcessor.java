package com.similan.service.internal.impl.event.processor.collaborationworkspace;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceWallEntry;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceRepository;
import com.similan.domain.repository.wall.collaborationworkspace.CollaborationWorkspaceWallEntryRepository;
import com.similan.service.internal.api.event.io.collaborationworkspace.CollaborationWorkspaceEvent;
import com.similan.service.internal.api.wall.WallInternalService;
import com.similan.service.internal.impl.event.EventPreProcessor;

public abstract class CollaborationWorkspaceEventProcessor<T extends CollaborationWorkspaceEvent>
    implements EventPreProcessor<T> {
  @Autowired
  private WallInternalService wallInternalService;
  @Autowired
  @Getter
  private CollaborationWorkspaceWallEntryRepository collaborationWorkspaceWallEntryRepository;
  @Autowired
  @Getter
  private CollaborationWorkspaceRepository collaborationWorkspaceRepository;

  public WallInternalService getWallInternalService() {
    return wallInternalService;
  }

  protected abstract CollaborationWorkspaceWallEntry createWallEntry(T event);

  protected Wall getWall(CollaborationWorkspace workspace) {
    return wallInternalService.get(workspace);
  }

  @Override
  @Transactional
  public void preProcess(T event) {
    CollaborationWorkspaceWallEntry entry = createWallEntry(event);
    wallInternalService.post(entry);
  }

}
