package com.similan.domain.repository.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.audit.AuditEventAttribute;
import com.similan.domain.repository.audit.jpa.JpaAuditEventAttributeRepository;
import com.similan.service.api.audit.dto.basic.AuditEventAttributeType;

@Repository
public class AuditEventAttributeRepository {
  @Autowired
  private JpaAuditEventAttributeRepository auditEventRepository;

  public AuditEventAttribute save(AuditEventAttribute attr) {
    return this.auditEventRepository.save(attr);
  }

  public AuditEventAttribute create(AuditEventAttributeType attributeType,
      String value) {

    AuditEventAttribute attr = new AuditEventAttribute();
    attr.setAttributeType(attributeType);
    attr.setValue(value);
    return attr;
  }

}
