package com.similan.service.api.wall.dto.basic.collaborationworkspace;

import java.util.Date;

import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.comment.dto.basic.CommentDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public class DocumentCommentWallEntryDto extends
    CollaborationWorkspaceDocumentWallEntryDto {

  private CommentDto<SharedDocumentKey> docComment;

  protected DocumentCommentWallEntryDto() {

  }

  public DocumentCommentWallEntryDto(SharedDocumentDto sharedDocument,
      ActorDto downloaderDto,
      WallEntryKey<CollaborationWorkspaceKey> key, Date date,
      Integer documentVersion, CommentDto<SharedDocumentKey> docComment) {
    super(sharedDocument, downloaderDto, key, date, documentVersion);
    this.docComment = docComment;
  }

  public CommentDto<SharedDocumentKey> getDocComment() {
    return docComment;
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_COMMENT;
  }

  @Override
  public String toString() {
    return "DocumentCommentWallEntryDto [docComment=" + docComment + "]";
  }

}
