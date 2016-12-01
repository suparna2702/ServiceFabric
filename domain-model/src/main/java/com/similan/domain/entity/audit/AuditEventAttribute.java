package com.similan.domain.entity.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.Setter;

import com.similan.domain.common.DomainBaseEntity;
import com.similan.service.api.audit.dto.basic.AuditEventAttributeType;

@Entity(name = "AuditEventAttribute")
@Getter
@Setter
public class AuditEventAttribute extends DomainBaseEntity {

  @Enumerated(EnumType.STRING)
  @Column
  private AuditEventAttributeType attributeType;

  @Column(length = 500)
  private String value;

}
