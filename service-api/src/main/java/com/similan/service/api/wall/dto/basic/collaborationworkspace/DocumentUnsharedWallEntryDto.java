package com.similan.service.api.wall.dto.basic.collaborationworkspace;

import java.util.Date;

import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public class DocumentUnsharedWallEntryDto extends
    CollaborationWorkspaceDocumentWallEntryDto {

  protected DocumentUnsharedWallEntryDto() {

  }

  public DocumentUnsharedWallEntryDto(SharedDocumentDto sharedDocument,
      ActorDto downloaderDto, WallEntryKey<CollaborationWorkspaceKey> key,
      Date date, Integer documentVersion) {
    super(sharedDocument, downloaderDto, key, date, documentVersion);
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_UNSHARED;
  }

}
