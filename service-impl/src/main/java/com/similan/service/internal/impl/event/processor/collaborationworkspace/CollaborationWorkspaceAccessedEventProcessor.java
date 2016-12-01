package com.similan.service.internal.impl.event.processor.collaborationworkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceWallEntry;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceRepository;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.service.api.wall.dto.basic.collaborationworkspace.CollaborationWorkspaceAccessedType;
import com.similan.service.internal.api.event.io.collaborationworkspace.CollaborationWorkspaceAccessedEvent;

@Component
public class CollaborationWorkspaceAccessedEventProcessor extends
    CollaborationWorkspaceEventProcessor<CollaborationWorkspaceAccessedEvent> {
  @Autowired
  private SocialActorRepository socialActorRepository;
  @Autowired
  private CollaborationWorkspaceRepository collaborationWorkspaceRepository;

	@Override
	protected CollaborationWorkspaceWallEntry createWallEntry(
			CollaborationWorkspaceAccessedEvent event) {
		long accessedWorkspaceId = event.getAccessedWorkspace();
		long accessorId = event.getAccessor();
		CollaborationWorkspaceAccessedType accessType = event.getAccessType();
		
		/* get accessed workspace */
		CollaborationWorkspace accessedWorkspace = this.collaborationWorkspaceRepository
				 .findOne(accessedWorkspaceId);
				
	    /* get social actor */
		SocialActor accessor = this.socialActorRepository.findOne(accessorId);
		Wall wall = getWall(accessedWorkspace);
		
		return getCollaborationWorkspaceWallEntryRepository()
				.createWorkspaceAccessedWallEntry(wall, accessor, accessedWorkspace, accessType);
	}

}
