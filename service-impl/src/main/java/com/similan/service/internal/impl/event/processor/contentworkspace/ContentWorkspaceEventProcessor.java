package com.similan.service.internal.impl.event.processor.contentworkspace;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceWallEntry;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.domain.repository.managementworkspace.ManagementWorkspaceRepository;
import com.similan.domain.repository.wall.contentspace.ContentWorkspaceWallEntryRepository;
import com.similan.service.internal.api.event.io.contentworkspace.ContentWorkspaceEvent;
import com.similan.service.internal.api.wall.WallInternalService;
import com.similan.service.internal.impl.event.EventPreProcessor;

public abstract class ContentWorkspaceEventProcessor<T extends ContentWorkspaceEvent>
    implements EventPreProcessor<T> {
  @Autowired
  private WallInternalService wallInternalService;
  @Autowired
  @Getter
  private ContentWorkspaceWallEntryRepository contentWorkspaceWallEntryRepository;
  @Autowired
  @Getter
  private SocialActorRepository socialActorRepository;
  @Autowired
  @Getter
  private ManagementWorkspaceRepository managementWorkspaceRepository;

  public WallInternalService getWallInternalService() {
    return wallInternalService;
  }

  protected abstract ContentWorkspaceWallEntry createWallEntry(T event);

  protected Wall getWall(ManagementWorkspace workspace) {
    return wallInternalService.get(workspace);
  }

  @Override
  @Transactional
  public void preProcess(T event) {
    ContentWorkspaceWallEntry entry = createWallEntry(event);
    wallInternalService.post(entry);
  }

}
