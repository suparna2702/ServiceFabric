package com.similan.service.internal.impl.event.processor.collaborationworkspace;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceWallEntry;
import com.similan.domain.repository.collaborationworkspace.SharedDocumentRepository;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.service.internal.api.event.io.collaborationworkspace.DocumentUpdatEvent;

@Slf4j
@Component
public class DocumentUpdatedEventProcessor extends
    CollaborationWorkspaceEventProcessor<DocumentUpdatEvent> {
  @Autowired
  private SharedDocumentRepository sharedDocumentRepository;
  @Autowired
  private SocialActorRepository socialActorRepository;

  @Override
  protected CollaborationWorkspaceWallEntry createWallEntry(
      DocumentUpdatEvent event) {

    log.info("Document Update Event " + event);

    Long sharedDocumentId = event.getSharedDocument();
    SharedDocument sharedDocument = sharedDocumentRepository
        .findOne(sharedDocumentId);

    Long updaterId = event.getUpdater();
    SocialActor updater = this.socialActorRepository.findOne(updaterId);

    CollaborationWorkspace workspace = sharedDocument.getWorkspace();
    Wall wall = getWall(workspace);

    return getCollaborationWorkspaceWallEntryRepository()
        .createDocumentUpdatedWallEntry(wall, sharedDocument, updater);
  }

}
