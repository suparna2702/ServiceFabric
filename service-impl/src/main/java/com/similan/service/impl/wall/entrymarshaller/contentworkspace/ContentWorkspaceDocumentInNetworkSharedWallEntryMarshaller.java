package com.similan.service.impl.wall.entrymarshaller.contentworkspace;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceDocumentInNetworkSharedWallEntry;
import com.similan.domain.share.ISharable;
import com.similan.domain.share.InNetworkShared;
import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.contentspace.ContentWorkspaceDocumentInNetworkSharedWallEntryDto;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.document.DocumentMarshaller;
import com.similan.service.impl.wall.WallEntryMarshaller;

@Component
public class ContentWorkspaceDocumentInNetworkSharedWallEntryMarshaller
    extends
    WallEntryMarshaller<ContentWorkspaceDocumentInNetworkSharedWallEntry, ManagementWorkspaceKey> {
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Autowired
  private DocumentMarshaller documentMarshaller;
  @Autowired
  private SocialActorMarshaller actorMarshaller;

  @Override
  public WallEntryDto<ManagementWorkspaceKey> marshall(
      WallEntryKey<ManagementWorkspaceKey> entryKey,
      ActorDto initiatorDto, Date date,
      ContentWorkspaceDocumentInNetworkSharedWallEntry entry) {

    InNetworkShared shared = entry.getInNetworkShared();
    ActorDto sharedTo = actorMarshaller
        .marshallActor(shared.getSharedToActor());

    GenericReference<ISharable> sharedDocRef = shared.getSharedEntity();
    IKeyHolderDto<DocumentKey> sharedDocumentKey = genericReferenceMarshaller
        .marshall(sharedDocRef, DocumentKey.class);

    Document document = documentMarshaller
        .unmarshallDocumentKey(sharedDocumentKey.getKey(), true);

    DocumentDto documentDto = documentMarshaller
        .marshallDocument(document);

    ContentWorkspaceDocumentInNetworkSharedWallEntryDto returnDto = new ContentWorkspaceDocumentInNetworkSharedWallEntryDto(
        documentDto, initiatorDto, entryKey, date, document.getLastInstance()
            .getVersion(), sharedTo);

    return returnDto;
  }
}
