package com.similan.service.api.wall.dto.basic.collaborationworkspace;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.profile.dto.ExternalSocialPersonDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.WallEntryKey;

@Getter
@Setter
@ToString
public class CollaborationWorkspaceExternalSharedWallEntryDto extends
    CollaborationWorkspaceDocumentWallEntryDto {

  private String header;

  private String message;

  private ExternalSocialPersonDto sharedWith;

  public CollaborationWorkspaceExternalSharedWallEntryDto(
      WallEntryKey<CollaborationWorkspaceKey> key,
      ActorDto initiator, Date date,
      SharedDocumentDto sharedDocument, Integer docVersion, String header,
      String message, ExternalSocialPersonDto sharedWith) {

    super(sharedDocument, initiator, key, date, docVersion);
    this.header = header;
    this.message = message;
    this.sharedWith = sharedWith;
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_EXTERNAL_SHARED;
  }

}
