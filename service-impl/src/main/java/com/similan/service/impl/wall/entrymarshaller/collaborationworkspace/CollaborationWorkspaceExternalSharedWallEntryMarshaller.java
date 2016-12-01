package com.similan.service.impl.wall.entrymarshaller.collaborationworkspace;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.ExternalSocialPerson;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceExternalSharedWallEntry;
import com.similan.domain.share.ExternalShared;
import com.similan.domain.share.ISharable;
import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.profile.dto.ExternalSocialPersonDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.collaborationworkspace.CollaborationWorkspaceExternalSharedWallEntryDto;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.impl.collaborationworkspace.CollaborationWorkspaceMarshaller;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.wall.WallEntryMarshaller;

@Slf4j
@Component
public class CollaborationWorkspaceExternalSharedWallEntryMarshaller
    extends
    WallEntryMarshaller<CollaborationWorkspaceExternalSharedWallEntry, CollaborationWorkspaceKey> {
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private CollaborationWorkspaceMarshaller collaborationWorkspaceMarshaller;
  
  @Override
  public WallEntryDto<CollaborationWorkspaceKey> marshall(
      WallEntryKey<CollaborationWorkspaceKey> entryKey,
      ActorDto initiatorDto, Date date,
      CollaborationWorkspaceExternalSharedWallEntry entry) {

    ExternalShared shared = entry.getExternalShared();
    log.info("Converting external shared wall entry to DTO for extenal shared "
            + shared.getSharedName());

    ExternalSocialPerson externalPerson = shared.getSharedTo();
    ExternalSocialPersonDto sharedWith = actorMarshaller.marshallExternalSocialPersonDto(externalPerson);

    GenericReference<ISharable> sharedContent = shared.getSharedEntity();
    IKeyHolderDto<SharedDocumentKey> sharedDocumentKey = genericReferenceMarshaller
        .marshall(sharedContent, SharedDocumentKey.class);

    SharedDocument sharedDocument = collaborationWorkspaceMarshaller
        .unmarshallSharedDocumentKey(sharedDocumentKey.getKey(), true);
    SharedDocumentDto sharedDocumentDto = collaborationWorkspaceMarshaller
        .marshallSharedDocument(sharedDocument);

    CollaborationWorkspaceExternalSharedWallEntryDto wallEntryDto = new CollaborationWorkspaceExternalSharedWallEntryDto(
        entryKey, initiatorDto, date, sharedDocumentDto, sharedDocument
            .getDocument().getLastInstance().getVersion(), shared.getHeader(),
        shared.getMessage(), sharedWith);

    return wallEntryDto;
  }
}
