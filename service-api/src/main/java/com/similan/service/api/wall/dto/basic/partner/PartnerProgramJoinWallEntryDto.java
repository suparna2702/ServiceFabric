package com.similan.service.api.wall.dto.basic.partner;

import java.util.Date;

import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public class PartnerProgramJoinWallEntryDto extends PartnerProgramWallEntryDto {

  private ActorDto partner;

  public PartnerProgramJoinWallEntryDto(
      WallEntryKey<CollaborationWorkspaceKey> key,
      ActorDto initiator, Date date, String programName,
      Long programId, ActorDto partner) {
    super(key, initiator, date, programName, programId);
    this.partner = partner;
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.PARTNER_JOINED;
  }

  public ActorDto getPartner() {
    return partner;
  }

}
