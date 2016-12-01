package com.similan.service.api.audit;

import java.util.List;

import com.similan.service.api.audit.dto.basic.AuditEventDto;
import com.similan.service.api.audit.dto.basic.AuditEventType;
import com.similan.service.api.audit.dto.operation.NewAuditEventDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public interface AuditEventService {
  public AuditEventDto create(NewAuditEventDto event, SocialActorKey member);

  public AuditEventDto create(NewAuditEventDto event, SocialActorKey member,
      SocialActorKey business);

  public AuditEventDto getLatest(AuditEventType eventType,
      SocialActorKey business);

  public List<AuditEventDto> getEventsForBusiness(AuditEventType eventType,
      SocialActorKey business);

  public List<AuditEventDto> getEventsForMember(AuditEventType eventType,
      SocialActorKey member);

}
