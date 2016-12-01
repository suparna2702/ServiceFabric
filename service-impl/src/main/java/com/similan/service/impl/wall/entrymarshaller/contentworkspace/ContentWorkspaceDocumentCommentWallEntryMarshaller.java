package com.similan.service.impl.wall.entrymarshaller.contentworkspace;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceDocumentCommentWallEntry;
import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.comment.dto.basic.CommentDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.contentspace.ContentWorkspaceDocumentCommentWallEntryDto;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.impl.comment.CommentMarshaller;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.document.DocumentMarshaller;
import com.similan.service.impl.wall.WallEntryMarshaller;

@Component
public class ContentWorkspaceDocumentCommentWallEntryMarshaller
    extends
    WallEntryMarshaller<ContentWorkspaceDocumentCommentWallEntry, ManagementWorkspaceKey> {
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Autowired
  private DocumentMarshaller documentMarshaller;
  @Autowired
  private CommentMarshaller commentMarshaller;

  @Override
  public WallEntryDto<ManagementWorkspaceKey> marshall(
      WallEntryKey<ManagementWorkspaceKey> entryKey,
      ActorDto initiatorDto, Date date,
      ContentWorkspaceDocumentCommentWallEntry entry) {

    GenericReference<IWallEntrySubject> subject = entry.getSubject();
    IKeyHolderDto<DocumentKey> sharedDocumentKey = genericReferenceMarshaller
        .marshall(subject, IWallEntrySubjectKey.class);

    Document document = documentMarshaller
        .unmarshallDocumentKey(sharedDocumentKey.getKey(), true);

    DocumentDto documentDto = documentMarshaller
        .marshallDocument(document);

    CommentDto<DocumentKey> comment = commentMarshaller.marshallComment(entry.getDocComment());

    ContentWorkspaceDocumentCommentWallEntryDto entryDto = new ContentWorkspaceDocumentCommentWallEntryDto(
        documentDto, initiatorDto, entryKey, entry.getDate(),
        entry.getDocumentVersion(), comment);

    return entryDto;
  }

}
