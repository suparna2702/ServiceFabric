package com.similan.service.api.audit.dto.operation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.service.api.audit.dto.basic.AuditEventAttributeType;
import com.similan.service.api.base.dto.operation.OperationDto;

@Getter
@Setter
@ToString
public class NewAuditEventAttributeDto extends OperationDto {

  private AuditEventAttributeType attributeType;

  private String value;

}
