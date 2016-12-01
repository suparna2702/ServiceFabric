package com.similan.domain.repository.audit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.audit.AuditEvent;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.audit.jpa.JpaAuditEventRepository;
import com.similan.service.api.audit.dto.basic.AuditEventType;

@Repository
public class AuditEventRepository {
  @Autowired
  private JpaAuditEventRepository repository;

  public AuditEvent save(AuditEvent event) {
    return this.repository.save(event);
  }

  public AuditEvent findOne(Long id) {
    return this.repository.findOne(id);
  }

  public List<AuditEvent> findByEventType(AuditEventType eventType) {
    return this.repository.findByEventType(eventType);
  }

  public List<AuditEvent> findByEventTypeAndBusiness(AuditEventType eventType,
      SocialOrganization business) {
    return this.repository.findByEventTypeAndBusiness(eventType, business);
  }

  public AuditEvent findLatestEventByTypeAndBusiness(AuditEventType eventType,
      SocialOrganization business) {
    return this.repository
        .findLatestEventByTypeAndBusiness(eventType, business);
  }

  public List<AuditEvent> findByEventTypeAndMember(AuditEventType eventType,
      SocialPerson member) {
    return this.repository.findByEventTypeAndMember(eventType, member);
  }

  public AuditEvent create(AuditEventType eventType, SocialPerson member) {
    AuditEvent auditEvent = new AuditEvent();
    auditEvent.setMember(member);
    auditEvent.setEventType(eventType);

    return auditEvent;
  }

  public AuditEvent create(AuditEventType eventType, SocialPerson member,
      SocialOrganization business) {
    AuditEvent auditEvent = new AuditEvent();
    auditEvent.setMember(member);
    auditEvent.setEventType(eventType);
    auditEvent.setBusiness(business);

    return auditEvent;
  }

}
