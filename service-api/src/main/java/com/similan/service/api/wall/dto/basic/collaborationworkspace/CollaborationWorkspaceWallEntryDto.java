package com.similan.service.api.wall.dto.basic.collaborationworkspace;

import java.util.Date;

import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public abstract class CollaborationWorkspaceWallEntryDto extends
    WallEntryDto<CollaborationWorkspaceKey> {

  protected CollaborationWorkspaceWallEntryDto() {
  }

  public CollaborationWorkspaceWallEntryDto(
      WallEntryKey<CollaborationWorkspaceKey> key,
      ActorDto initiator, Date date) {
    super(key, initiator, date);
  }

}
