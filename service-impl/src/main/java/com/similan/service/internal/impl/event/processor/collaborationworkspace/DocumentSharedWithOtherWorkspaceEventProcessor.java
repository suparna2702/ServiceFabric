package com.similan.service.internal.impl.event.processor.collaborationworkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceWallEntry;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceRepository;
import com.similan.domain.repository.collaborationworkspace.SharedDocumentRepository;
import com.similan.service.internal.api.event.io.collaborationworkspace.DocumentSharedWithOtherWorkspaceEvent;

@Component
public class DocumentSharedWithOtherWorkspaceEventProcessor extends
    CollaborationWorkspaceEventProcessor<DocumentSharedWithOtherWorkspaceEvent> {
  @Autowired
  private SharedDocumentRepository sharedDocumentRepository;
  @Autowired
  private CollaborationWorkspaceRepository collaborationWorkspaceRepository;

	@Override
	protected CollaborationWorkspaceWallEntry createWallEntry(
			DocumentSharedWithOtherWorkspaceEvent event) {
		long sharedDocumentId = event.getSharedDocumentId();
	    SharedDocument sharedDocument = sharedDocumentRepository
	        .findOne(sharedDocumentId);
	    
	    CollaborationWorkspace workspaceTo = sharedDocument.getWorkspace();
	    long workspaceFromId = event.getWorkspaceFromId();
	    CollaborationWorkspace workspaceFrom = collaborationWorkspaceRepository.findOne(workspaceFromId);
	    
	    Wall wall = getWall(workspaceTo);
	    return getCollaborationWorkspaceWallEntryRepository()
	            .createDocumentSharedWithOtherWorkspaceWallEntry(wall, sharedDocument, workspaceFrom);
	}

}
