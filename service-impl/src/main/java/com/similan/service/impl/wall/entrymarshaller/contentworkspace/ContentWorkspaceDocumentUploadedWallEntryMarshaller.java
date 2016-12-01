package com.similan.service.impl.wall.entrymarshaller.contentworkspace;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceDocumentUploadedWallEntry;
import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.contentspace.ContentWorkspaceDocumentUploadedWallEntryDto;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.document.DocumentMarshaller;
import com.similan.service.impl.wall.WallEntryMarshaller;

@Component
public class ContentWorkspaceDocumentUploadedWallEntryMarshaller
    extends
    WallEntryMarshaller<ContentWorkspaceDocumentUploadedWallEntry, ManagementWorkspaceKey> {
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Autowired
  private DocumentMarshaller documentMarshaller;
  
  @Override
  public WallEntryDto<ManagementWorkspaceKey> marshall(
      WallEntryKey<ManagementWorkspaceKey> entryKey,
      ActorDto initiatorDto, Date date,
      ContentWorkspaceDocumentUploadedWallEntry entry) {

    GenericReference<IWallEntrySubject> subject = entry.getSubject();
    IKeyHolderDto<DocumentKey> sharedDocumentKey = genericReferenceMarshaller
        .marshall(subject, IWallEntrySubjectKey.class);

    Document document = documentMarshaller
        .unmarshallDocumentKey(sharedDocumentKey.getKey(), true);

    DocumentDto documentDto = documentMarshaller
        .marshallDocument(document);
    ContentWorkspaceDocumentUploadedWallEntryDto entryDto = new ContentWorkspaceDocumentUploadedWallEntryDto(
        documentDto, initiatorDto, entryKey, entry.getDate(),
        entry.getDocumentVersion());

    return entryDto;
  }

}
