package com.similan.service.api.advertisement.dto.basic;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.service.api.advertisement.dto.key.DisplayNoticeKey;
import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

@Getter
@Setter
@ToString
public class DisplayNoticeDto extends KeyHolderDto<DisplayNoticeKey> {

  private String name;
  
  private String uuid = UUID.randomUUID().toString();

  private SocialActorKey owner;

  private String iconAsset;

  private Boolean active;

  private Date createDate;

  public DisplayNoticeDto(DisplayNoticeKey key) {
    super(key);
  }

}
