package com.similan.service.api.wall.dto.basic.advertisement;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;

import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.WallEntryKey;

@Getter
@Setter
public class DisplayNoticeViewedWallEntryDto extends
    WallEntryDto<SocialActorKey> {
  
  private Long id;
  
  private String name;
  
  private String iconAsset;

  public DisplayNoticeViewedWallEntryDto(WallEntryKey<SocialActorKey> key,
      ActorDto initiator, Date date) {
    super(key, initiator, date);
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.DISPLAY_NOTICE_VIEWED;
  }

}
