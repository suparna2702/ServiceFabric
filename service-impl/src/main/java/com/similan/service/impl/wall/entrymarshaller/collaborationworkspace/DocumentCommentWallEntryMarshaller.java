package com.similan.service.impl.wall.entrymarshaller.collaborationworkspace;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.domain.entity.wall.collaborationworkspace.DocumentCommentWallEntry;
import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.comment.dto.basic.CommentDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.collaborationworkspace.DocumentCommentWallEntryDto;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.impl.collaborationworkspace.CollaborationWorkspaceMarshaller;
import com.similan.service.impl.comment.CommentMarshaller;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.wall.WallEntryMarshaller;

@Component
public class DocumentCommentWallEntryMarshaller extends
    WallEntryMarshaller<DocumentCommentWallEntry, CollaborationWorkspaceKey> {
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Autowired
  private CollaborationWorkspaceMarshaller collaborationWorkspaceMarshaller;
  @Autowired
  private CommentMarshaller commentMarshaller;

  @Override
  public WallEntryDto<CollaborationWorkspaceKey> marshall(
      WallEntryKey<CollaborationWorkspaceKey> entryKey,
      ActorDto downloaderDto, Date date,
      DocumentCommentWallEntry entry) {

    GenericReference<IWallEntrySubject> subject = entry.getSubject();
    IKeyHolderDto<SharedDocumentKey> sharedDocumentKey = genericReferenceMarshaller
        .marshall(subject, IWallEntrySubjectKey.class);

    SharedDocument sharedDocument = collaborationWorkspaceMarshaller
        .unmarshallSharedDocumentKey(sharedDocumentKey.getKey(), true);

    SharedDocumentDto sharedDocumentDto = collaborationWorkspaceMarshaller
        .marshallSharedDocument(sharedDocument);

    CommentDto<SharedDocumentKey> doccomment = commentMarshaller.marshallComment(entry.getDocComment());

    DocumentCommentWallEntryDto entryDto = new DocumentCommentWallEntryDto(
        sharedDocumentDto, downloaderDto, entryKey, entry.getDate(),
        entry.getDocumentVersion(), doccomment);
    return entryDto;

  }

}
