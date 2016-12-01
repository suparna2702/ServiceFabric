package com.similan.service.api.wall.dto.basic.network;

import java.util.Date;

import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public class BusinessProfileUpdateWallEntryDto extends
    SocialActorProfileWallEntryDto {

  public BusinessProfileUpdateWallEntryDto(ActorDto profileOwnerActor,
      WallEntryKey<SocialActorKey> key, ActorDto initiator, Date date) {
    super(profileOwnerActor, key, initiator, date);
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.BUSINESS_PROFILE_UPDATED;
  }

}
