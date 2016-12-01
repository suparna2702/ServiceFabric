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
import com.similan.service.internal.api.event.io.collaborationworkspace.DocumentUnsharedEvent;

@Component
public class DocumentUnsharedEventProcessor extends
    CollaborationWorkspaceEventProcessor<DocumentUnsharedEvent> {

  @Autowired
  private SharedDocumentRepository sharedDocumentRepository;
  @Autowired
  private SocialActorRepository actorRepository;

  @Override
  protected CollaborationWorkspaceWallEntry createWallEntry(
      DocumentUnsharedEvent event) {

    long sharedDocumentId = event.getSharedDocumentId();
    SharedDocument sharedDocument = sharedDocumentRepository
        .findOne(sharedDocumentId);

    long unsharerId = event.getUnsharer();
    SocialActor unsharer = actorRepository.findOne(unsharerId);

    CollaborationWorkspace workspace = sharedDocument.getWorkspace();
    Wall wall = getWall(workspace);

    return getCollaborationWorkspaceWallEntryRepository()
        .createDocumentUnsharedWallEntry(wall, sharedDocument, unsharer);
  }

}
