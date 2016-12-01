package com.similan.service.internal.impl.event.processor.collaborationworkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceWallEntry;
import com.similan.domain.repository.collaborationworkspace.SharedDocumentRepository;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.service.internal.api.event.io.collaborationworkspace.DocumentDownloadedEvent;

@Component
public class DocumentDownloadedEventProcessor extends
    CollaborationWorkspaceEventProcessor<DocumentDownloadedEvent> {
  @Autowired
  private SharedDocumentRepository sharedDocumentRepository;
  @Autowired
  private SocialActorRepository socialActorRepository;

  @Override
  public CollaborationWorkspaceWallEntry createWallEntry(
      DocumentDownloadedEvent event) {
    long sharedDocumentId = event.getSharedDocumentId();
    SharedDocument sharedDocument = sharedDocumentRepository
        .findOne(sharedDocumentId);
    long socialActorId = event.getDownloaderId();
    SocialActor downLoader = socialActorRepository.findOne(socialActorId);

    CollaborationWorkspace workspace = sharedDocument.getWorkspace();
    Wall wall = getWall(workspace);
    return getCollaborationWorkspaceWallEntryRepository()
        .createDocumentDownloadedEntry(wall, downLoader, sharedDocument);

  }
}
