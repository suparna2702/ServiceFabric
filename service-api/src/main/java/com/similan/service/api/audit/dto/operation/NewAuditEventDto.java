package com.similan.service.api.audit.dto.operation;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.service.api.audit.dto.basic.AuditEventType;
import com.similan.service.api.base.dto.operation.OperationDto;

@Getter
@Setter
@ToString
public class NewAuditEventDto extends OperationDto {

  private AuditEventType eventType;

  private List<NewAuditEventAttributeDto> attributes;

}
