package com.similan.service.api.audit.dto.basic;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AuditEventAttributeDto {

  private AuditEventAttributeType attributeType;

  private String value;

  public AuditEventAttributeDto(AuditEventAttributeType attributeType,
      String value) {
    this.attributeType = attributeType;
    this.value = value;
  }

}
