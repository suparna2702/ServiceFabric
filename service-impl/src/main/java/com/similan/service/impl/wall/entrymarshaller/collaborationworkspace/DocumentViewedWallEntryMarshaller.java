package com.similan.service.impl.wall.entrymarshaller.collaborationworkspace;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.domain.entity.wall.collaborationworkspace.DocumentViewedWallEntry;
import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.collaborationworkspace.DocumentViewedWallEntryDto;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.impl.collaborationworkspace.CollaborationWorkspaceMarshaller;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.wall.WallEntryMarshaller;

@Component
public class DocumentViewedWallEntryMarshaller extends
                          WallEntryMarshaller<DocumentViewedWallEntry, CollaborationWorkspaceKey>{
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Autowired
  private CollaborationWorkspaceMarshaller collaborationWorkspaceMarshaller;
  
	@Override
	public WallEntryDto<CollaborationWorkspaceKey> marshall(
			WallEntryKey<CollaborationWorkspaceKey> entryKey,
			ActorDto initiatorDto, Date date,
			DocumentViewedWallEntry entry) {
		
		GenericReference<IWallEntrySubject> subject = entry.getSubject();
		IKeyHolderDto<SharedDocumentKey> sharedDocumentKey = genericReferenceMarshaller
                .marshall(subject, IWallEntrySubjectKey.class);
				
		SharedDocument sharedDocument = collaborationWorkspaceMarshaller
		        .unmarshallSharedDocumentKey(sharedDocumentKey.getKey(), true);
		
		SharedDocumentDto sharedDocumentDto = collaborationWorkspaceMarshaller
		        .marshallSharedDocument(sharedDocument);
		
		DocumentViewedWallEntryDto entryDto = new DocumentViewedWallEntryDto(sharedDocumentDto,
				  initiatorDto, entry.getDocumentVersion(), entryKey, date);
		return entryDto;
	}

}
