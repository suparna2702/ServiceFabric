package com.similan.service.api.advertisement.dto.operation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

@Getter
@Setter
@ToString
public class NewDisplayNoticeDto extends OperationDto {
  
  private String name;
  
  private String uuid;

  private SocialActorKey owner;
  
  private SocialActorKey creator;

  private String iconAsset;
  
  private String text;
  
  private String url;

}
