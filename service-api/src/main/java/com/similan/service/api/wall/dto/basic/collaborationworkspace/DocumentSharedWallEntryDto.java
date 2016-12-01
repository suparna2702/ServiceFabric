package com.similan.service.api.wall.dto.basic.collaborationworkspace;

import java.util.Date;

import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public class DocumentSharedWallEntryDto extends
    CollaborationWorkspaceDocumentWallEntryDto {

  protected DocumentSharedWallEntryDto() {
  }

  public DocumentSharedWallEntryDto(
      WallEntryKey<CollaborationWorkspaceKey> key,
      ActorDto initiator, Date date,
      SharedDocumentDto sharedDocument, Integer docVersion) {
    super(sharedDocument, initiator, key, date, docVersion);
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_SHARED;
  }

}
