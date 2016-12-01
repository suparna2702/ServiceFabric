package com.similan.service.api.wall.dto.basic.collaborationworkspace;

import java.util.Date;

import lombok.ToString;

import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.WallEntryKey;

@ToString
public class DocumentUpdateWallEntryDto extends
    CollaborationWorkspaceDocumentWallEntryDto {
  
  protected DocumentUpdateWallEntryDto() {

  }

  public DocumentUpdateWallEntryDto(SharedDocumentDto updatedDocument,
      ActorDto updaterDto, int documentVersion,
      WallEntryKey<CollaborationWorkspaceKey> key, Date date) {
    super(updatedDocument, updaterDto, key, date, documentVersion);
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_UPDATED;
  }


}
