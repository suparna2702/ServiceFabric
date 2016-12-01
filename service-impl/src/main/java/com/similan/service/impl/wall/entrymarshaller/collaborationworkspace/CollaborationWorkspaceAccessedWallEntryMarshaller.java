package com.similan.service.impl.wall.entrymarshaller.collaborationworkspace;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceAccessedWallEntry;
import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.collaborationworkspace.CollaborationWorkspaceAccessedType;
import com.similan.service.api.wall.dto.basic.collaborationworkspace.CollaborationWorkspaceAccessedWallEntryDto;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.impl.collaborationworkspace.CollaborationWorkspaceMarshaller;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.wall.WallEntryMarshaller;

@Component
public class CollaborationWorkspaceAccessedWallEntryMarshaller extends
               WallEntryMarshaller<CollaborationWorkspaceAccessedWallEntry, CollaborationWorkspaceKey>{
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Autowired
  private CollaborationWorkspaceMarshaller collaborationWorkspaceMarshaller;
  @Autowired
  private SocialActorMarshaller actorMarshaller;

	@Override
	public WallEntryDto<CollaborationWorkspaceKey> marshall(
			WallEntryKey<CollaborationWorkspaceKey> entryKey,
			ActorDto initiatorDto, Date date,
			CollaborationWorkspaceAccessedWallEntry entry) {
		
		GenericReference<IWallEntrySubject> subject = entry.getSubject();
		IKeyHolderDto<CollaborationWorkspaceKey> workspaceKey = genericReferenceMarshaller
                .marshall(subject, IWallEntrySubjectKey.class);
		
		CollaborationWorkspace workspace = collaborationWorkspaceMarshaller.unmarshallWorkspaceKey(workspaceKey.getKey(), true);
		CollaborationWorkspaceDto workspaceDto = collaborationWorkspaceMarshaller
				.marshallWorkspace(workspace);
		
		CollaborationWorkspaceAccessedType accessType = entry.getAccessType();
		
		SocialActor actor = entry.getInitiator();
		ActorDto accessor = actorMarshaller
				.marshallActor(actor);
		
		CollaborationWorkspaceAccessedWallEntryDto entryDto = new CollaborationWorkspaceAccessedWallEntryDto(workspaceDto, 
				accessor, accessType, entryKey, date);
		return entryDto;
	}
	

}
