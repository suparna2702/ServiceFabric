package com.similan.domain.repository.audit.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.audit.AuditEventAttribute;

public interface JpaAuditEventAttributeRepository extends
    JpaRepository<AuditEventAttribute, Long> {

}
