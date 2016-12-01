package com.similan.service.api.audit.dto.basic;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.service.api.audit.AuditEventKey;
import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

@Getter
@Setter
@ToString
public class AuditEventDto extends KeyHolderDto<AuditEventKey> {

  private AuditEventType eventType;

  private SocialActorKey member;

  private SocialActorKey business;

  private Date createdOn;
  
  private List<AuditEventAttributeDto> attributes;
  
  public AuditEventDto(){
    
  }

}
