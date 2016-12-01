package com.similan.domain.repository.audit.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.audit.AuditEvent;
import com.similan.domain.entity.community.SocialActor;
import com.similan.service.api.audit.dto.basic.AuditEventType;

public interface JpaAuditEventRepository extends
    JpaRepository<AuditEvent, Long> {

  public List<AuditEvent> findByEventType(AuditEventType eventType);

  public List<AuditEvent> findByEventTypeAndMember(AuditEventType eventType,
      SocialActor member);

  public List<AuditEvent> findByEventTypeAndBusiness(AuditEventType eventType,
      SocialActor business);

  @Query("select auditEvent from AuditEvent auditEvent"
      + " where auditEvent.createdOn = (select max(auditEventLatest.createdOn)"
      + " from AuditEvent auditEventLatest where auditEventLatest.eventType = :eventType"
      + " and auditEventLatest.business = :business)")
  public AuditEvent findLatestEventByTypeAndBusiness(
      @Param("eventType") AuditEventType eventType,
      @Param("business") SocialActor business);

}
