package com.similan.service.impl.audit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.audit.AuditEvent;
import com.similan.domain.entity.audit.AuditEventAttribute;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.audit.AuditEventAttributeRepository;
import com.similan.domain.repository.audit.AuditEventRepository;
import com.similan.service.api.audit.AuditEventService;
import com.similan.service.api.audit.dto.basic.AuditEventDto;
import com.similan.service.api.audit.dto.basic.AuditEventType;
import com.similan.service.api.audit.dto.operation.NewAuditEventAttributeDto;
import com.similan.service.api.audit.dto.operation.NewAuditEventDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.wall.entrymarshaller.audit.AuditEventMarshaller;

@Service
public class AuditEventServiceImpl extends ServiceImpl implements
    AuditEventService {
  @Autowired
  private AuditEventRepository auditEventRepository;
  @Autowired
  private AuditEventAttributeRepository auditEventAttributeRepository;
  @Autowired
  private AuditEventMarshaller auditEventMarshaller;
  @Autowired
  private SocialActorMarshaller actorMarshaller;

  @Override
  @Transactional
  public AuditEventDto create(NewAuditEventDto event, SocialActorKey memberKey) {
    SocialActor member = actorMarshaller.unmarshallActorKey(memberKey, true);
    AuditEvent auditEvent = auditEventRepository.create(event.getEventType(),
        (SocialPerson) member);
    auditEventRepository.save(auditEvent);

    // save the attributes
    List<NewAuditEventAttributeDto> eventAttributes = event.getAttributes();
    if (eventAttributes != null) {
      for (NewAuditEventAttributeDto eventAttribute : eventAttributes) {
        AuditEventAttribute attribute = this.auditEventAttributeRepository
            .create(eventAttribute.getAttributeType(),
                eventAttribute.getValue());
        this.auditEventAttributeRepository.save(attribute);
        auditEvent.getAttributes().add(attribute);
      }
      auditEventRepository.save(auditEvent);
    }

    AuditEventDto retDto = this.auditEventMarshaller.marshall(auditEvent);
    return retDto;
  }

  @Override
  @Transactional
  public AuditEventDto create(NewAuditEventDto event, SocialActorKey memberKey,
      SocialActorKey businessKey) {
    SocialActor member = actorMarshaller.unmarshallActorKey(memberKey, true);
    SocialActor business = actorMarshaller
        .unmarshallActorKey(businessKey, true);
    AuditEvent auditEvent = auditEventRepository.create(event.getEventType(),
        (SocialPerson) member, (SocialOrganization) business);
    auditEventRepository.save(auditEvent);

    // save the attributes
    List<NewAuditEventAttributeDto> eventAttributes = event.getAttributes();
    if (eventAttributes != null) {
      for (NewAuditEventAttributeDto eventAttribute : eventAttributes) {
        AuditEventAttribute attribute = this.auditEventAttributeRepository
            .create(eventAttribute.getAttributeType(),
                eventAttribute.getValue());
        this.auditEventAttributeRepository.save(attribute);
        auditEvent.getAttributes().add(attribute);
      }
      auditEventRepository.save(auditEvent);
    }

    AuditEventDto retDto = this.auditEventMarshaller.marshall(auditEvent);
    return retDto;
  }

  @Override
  @Transactional
  public AuditEventDto getLatest(AuditEventType eventType,
      SocialActorKey businessKey) {
    SocialActor business = actorMarshaller
        .unmarshallActorKey(businessKey, true);
    AuditEvent auditEvent = this.auditEventRepository
        .findLatestEventByTypeAndBusiness(eventType,
            (SocialOrganization) business);

    if (auditEvent != null) {
      AuditEventDto retDto = this.auditEventMarshaller.marshall(auditEvent);
      return retDto;
    } else {
      return null;
    }

  }

  @Override
  @Transactional
  public List<AuditEventDto> getEventsForBusiness(AuditEventType eventType,
      SocialActorKey businessKey) {
    SocialActor business = actorMarshaller
        .unmarshallActorKey(businessKey, true);
    List<AuditEvent> auditEventList = this.auditEventRepository
        .findByEventTypeAndBusiness(eventType, (SocialOrganization) business);

    if (auditEventList != null) {
      List<AuditEventDto> eventListDto = this.auditEventMarshaller
          .marshall(auditEventList);
      return eventListDto;
    } else {
      return null;
    }

  }

  @Override
  @Transactional
  public List<AuditEventDto> getEventsForMember(AuditEventType eventType,
      SocialActorKey memberKey) {
    SocialActor member = actorMarshaller.unmarshallActorKey(memberKey, true);
    List<AuditEvent> auditEventList = this.auditEventRepository
        .findByEventTypeAndMember(eventType, (SocialPerson) member);

    if (auditEventList != null) {
      List<AuditEventDto> eventListDto = this.auditEventMarshaller
          .marshall(auditEventList);
      return eventListDto;
    } else {
      return null;
    }

  }

}
