package com.similan.service.api.wall.dto.basic.partner;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public abstract class PartnerProgramWallEntryDto extends
    WallEntryDto<CollaborationWorkspaceKey> {

  private String programName;

  private Long programId;

  public PartnerProgramWallEntryDto(
      WallEntryKey<CollaborationWorkspaceKey> key, ActorDto initiator,
      Date date, String programName, Long programId) {
    super(key, initiator, date);
    this.programName = programName;
    this.programId = programId;
  }

  public String getProgramName() {
    return programName;
  }

  public Long getProgramId() {
    return programId;
  }

}
