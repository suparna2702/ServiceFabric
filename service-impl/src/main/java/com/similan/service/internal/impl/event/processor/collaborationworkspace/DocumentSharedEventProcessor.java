package com.similan.service.internal.impl.event.processor.collaborationworkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceWallEntry;
import com.similan.domain.repository.collaborationworkspace.SharedDocumentRepository;
import com.similan.service.internal.api.event.io.collaborationworkspace.DocumentSharedEvent;

@Component
public class DocumentSharedEventProcessor extends
    CollaborationWorkspaceEventProcessor<DocumentSharedEvent> {
  @Autowired
  private SharedDocumentRepository sharedDocumentRepository;

  @Override
  protected CollaborationWorkspaceWallEntry createWallEntry(
      DocumentSharedEvent event) {
    long sharedDocumentId = event.getSharedDocumentId();
    SharedDocument sharedDocument = sharedDocumentRepository
        .findOne(sharedDocumentId);
    CollaborationWorkspace workspace = sharedDocument.getWorkspace();
    Wall wall = getWall(workspace);
    return getCollaborationWorkspaceWallEntryRepository()
        .createDocumentSharedEntry(wall, sharedDocument);
  }
}
