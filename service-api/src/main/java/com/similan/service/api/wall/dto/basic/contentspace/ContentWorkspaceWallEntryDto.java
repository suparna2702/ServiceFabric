package com.similan.service.api.wall.dto.basic.contentspace;

import java.util.Date;

import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public abstract class ContentWorkspaceWallEntryDto extends
    WallEntryDto<ManagementWorkspaceKey> {

  protected ContentWorkspaceWallEntryDto() {

  }

  public ContentWorkspaceWallEntryDto(WallEntryKey<ManagementWorkspaceKey> key,
      ActorDto initiator, Date date) {
    super(key, initiator, date);
  }

}
