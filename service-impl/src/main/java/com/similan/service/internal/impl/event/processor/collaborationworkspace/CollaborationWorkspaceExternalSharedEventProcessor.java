package com.similan.service.internal.impl.event.processor.collaborationworkspace;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceWallEntry;
import com.similan.domain.repository.collaborationworkspace.SharedDocumentRepository;
import com.similan.domain.repository.share.ExternalSharedRepository;
import com.similan.domain.share.ExternalShared;
import com.similan.domain.share.ISharable;
import com.similan.service.internal.api.event.io.collaborationworkspace.CollaborationWorkspaceExternalSharedEvent;

@Slf4j
@Component
public class CollaborationWorkspaceExternalSharedEventProcessor
    extends
    CollaborationWorkspaceEventProcessor<CollaborationWorkspaceExternalSharedEvent> {
  @Autowired
  private ExternalSharedRepository externalSharedRepository;
  @Autowired
  private SharedDocumentRepository sharedDocumentRepository;

  @Override
  protected CollaborationWorkspaceWallEntry createWallEntry(
      CollaborationWorkspaceExternalSharedEvent event) {

    log.info("Received event " + event);

    CollaborationWorkspace workspace = this.getCollaborationWorkspaceRepository()
        .findOne(event.getSharedFromWorkspace());
    ExternalShared shared = this.externalSharedRepository.findOne(event
        .getExternalShared());
    Wall wall = getWall(workspace);

    GenericReference<ISharable> sharedContent = shared.getSharedEntity();
    SharedDocument sharedDocument = this.sharedDocumentRepository
        .findOne(sharedContent.getId());

    return this.getCollaborationWorkspaceWallEntryRepository()
        .createCollaborationWorkspaceExternalSharedWallEntry(wall, shared,
            sharedDocument);
  }

}
