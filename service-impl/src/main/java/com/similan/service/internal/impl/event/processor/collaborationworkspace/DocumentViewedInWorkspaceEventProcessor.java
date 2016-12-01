package com.similan.service.internal.impl.event.processor.collaborationworkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.DocumentViewedWallEntry;
import com.similan.domain.repository.collaborationworkspace.SharedDocumentRepository;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.service.internal.api.event.io.collaborationworkspace.DocumentViewedInWorkspaceEvent;

@Component
public class DocumentViewedInWorkspaceEventProcessor extends
                                CollaborationWorkspaceEventProcessor<DocumentViewedInWorkspaceEvent>{
  @Autowired
  private SharedDocumentRepository sharedDocumentRepository;
  @Autowired
  private SocialActorRepository socialActorRepository;

	@Override
	protected CollaborationWorkspaceWallEntry createWallEntry(
			DocumentViewedInWorkspaceEvent event) {
		SharedDocument viewedDocument = this.sharedDocumentRepository.findOne(event.getViewedDocumentId());
		SocialActor viewer = this.socialActorRepository.findOne(event.getViewerId());
		Wall wall = getWall(viewedDocument.getWorkspace());
		DocumentViewedWallEntry wallEntry = this.getCollaborationWorkspaceWallEntryRepository()
				.createDocumentViewedWallEntry(wall, viewedDocument, viewer);
		
		return wallEntry;
	}

}
