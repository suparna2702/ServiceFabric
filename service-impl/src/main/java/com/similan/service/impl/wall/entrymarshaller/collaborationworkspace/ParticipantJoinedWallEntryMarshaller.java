package com.similan.service.impl.wall.entrymarshaller.collaborationworkspace;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceParticipation;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.domain.entity.wall.collaborationworkspace.ParticipantJoinedWallEntry;
import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceParticipationDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceParticipationKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.collaborationworkspace.ParticipantJoinedWallEntryDto;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.impl.collaborationworkspace.CollaborationWorkspaceMarshaller;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.wall.WallEntryMarshaller;

@Component
public class ParticipantJoinedWallEntryMarshaller extends
    WallEntryMarshaller<ParticipantJoinedWallEntry, CollaborationWorkspaceKey> {
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Autowired
  private CollaborationWorkspaceMarshaller collaborationWorkspaceMarshaller;

  @Override
  public WallEntryDto<CollaborationWorkspaceKey> marshall(
      WallEntryKey<CollaborationWorkspaceKey> entryKey,
      ActorDto initiatorDto, Date date,
      ParticipantJoinedWallEntry entry) {
	  
    GenericReference<IWallEntrySubject> subject = entry.getSubject();
	IKeyHolderDto<CollaborationWorkspaceParticipationKey> participationKey = genericReferenceMarshaller
	                   .marshall(subject, IWallEntrySubjectKey.class);
	
    CollaborationWorkspaceParticipation participation = collaborationWorkspaceMarshaller.unmarshallParticipationKey(participationKey.getKey(), true);
        
    CollaborationWorkspaceParticipationDto participationDto = collaborationWorkspaceMarshaller.marshallParticipation(
            participation);
    
    ParticipantJoinedWallEntryDto entryDto = new ParticipantJoinedWallEntryDto(
        entryKey, initiatorDto, date, participationDto);
    return entryDto;
  }
}
