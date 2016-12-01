package com.similan.service.api.wall.dto.basic.collaborationworkspace;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public class DocumentSharedWithOtherWorkspaceWallEntryDto extends
    CollaborationWorkspaceDocumentWallEntryDto {
  @XmlElement
  private CollaborationWorkspaceKey sharedFromSpace;

  protected DocumentSharedWithOtherWorkspaceWallEntryDto() {

  }

  public DocumentSharedWithOtherWorkspaceWallEntryDto(
      SharedDocumentDto sharedWithDocument, ActorDto sharerDto,
      CollaborationWorkspaceKey sharedWithSpace,
      WallEntryKey<CollaborationWorkspaceKey> key, Date date,
      Integer documentVersion) {
    super(sharedWithDocument, sharerDto, key, date, documentVersion);
    this.sharedFromSpace = sharedWithSpace;
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_SHARED_ANOTHER_WORKSPACE;
  }

  public CollaborationWorkspaceKey getSharedFromSpace() {
    return sharedFromSpace;
  }

}
