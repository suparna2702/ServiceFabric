package com.similan.service.impl.wall.entrymarshaller.audit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.audit.AuditEvent;
import com.similan.domain.entity.audit.AuditEventAttribute;
import com.similan.domain.repository.audit.AuditEventRepository;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.service.api.audit.dto.basic.AuditEventAttributeDto;
import com.similan.service.api.audit.dto.basic.AuditEventDto;
import com.similan.service.impl.Marshaller;
import com.similan.service.impl.community.SocialActorMarshaller;

@Component
public class AuditEventMarshaller extends Marshaller {
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private SocialActorRepository actorRepository;
  @Autowired
  private AuditEventRepository eventRepository;

  public List<AuditEventDto> marshall(List<AuditEvent> events) {
    List<AuditEventDto> eventListDto = new ArrayList<AuditEventDto>();
    for (AuditEvent event : events) {
      AuditEventDto eventDto = this.marshall(event);
      eventListDto.add(eventDto);
    }
    return eventListDto;
  }

  public AuditEventDto marshall(AuditEvent event) {
    AuditEventDto eventDto = new AuditEventDto();
 
    if (event.getMember() != null) {
      eventDto.setMember(actorMarshaller.marshallActorKey(event.getMember()));
    }
    if (event.getBusiness() != null) {
      eventDto
          .setBusiness(actorMarshaller.marshallActorKey(event.getBusiness()));
    }
    eventDto.setCreatedOn(event.getCreatedOn());
    eventDto.setEventType(event.getEventType());
    eventDto.setAttributes(this.marshallAttributes(event.getAttributes()));

    return eventDto;
  }

  public List<AuditEventAttributeDto> marshallAttributes(
      List<AuditEventAttribute> attrs) {
    List<AuditEventAttributeDto> retList = new ArrayList<AuditEventAttributeDto>();
    for (AuditEventAttribute attr : attrs) {
      AuditEventAttributeDto attrDto = this.marshall(attr);
      retList.add(attrDto);
    }
    return retList;
  }

  public AuditEventAttributeDto marshall(AuditEventAttribute attr) {
    AuditEventAttributeDto attrDto = new AuditEventAttributeDto(
        attr.getAttributeType(), attr.getValue());
    return attrDto;

  }
}
