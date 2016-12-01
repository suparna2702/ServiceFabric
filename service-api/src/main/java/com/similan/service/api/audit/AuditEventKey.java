package com.similan.service.api.audit;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

public class AuditEventKey extends Key {

  public AuditEventKey() {

  }

  public AuditEventKey(Long id) {
    this.setId(id);
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.AUDIT_EVENT;
  }

}
