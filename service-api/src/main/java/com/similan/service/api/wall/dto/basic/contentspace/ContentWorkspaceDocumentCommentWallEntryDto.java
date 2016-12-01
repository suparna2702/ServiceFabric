package com.similan.service.api.wall.dto.basic.contentspace;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.similan.service.api.comment.dto.basic.CommentDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public class ContentWorkspaceDocumentCommentWallEntryDto extends
    ContentWorkspaceDocumentWallEntryDto {

  private CommentDto<DocumentKey> docComment;

  protected ContentWorkspaceDocumentCommentWallEntryDto() {

  }

  public ContentWorkspaceDocumentCommentWallEntryDto(DocumentDto document,
      ActorDto initiatorDto, WallEntryKey<ManagementWorkspaceKey> key,
      Date date, Integer documentVersion, CommentDto<DocumentKey> docComment) {
    super(document, initiatorDto, key, date, documentVersion);
    this.docComment = docComment;
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.CONTENT_WORKSPACE_DOCUMENT_COMMENT;
  }

  public CommentDto<DocumentKey> getDocComment() {
    return docComment;
  }

}
