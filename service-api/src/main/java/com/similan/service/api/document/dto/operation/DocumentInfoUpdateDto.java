package com.similan.service.api.document.dto.operation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.service.api.base.dto.operation.OperationDto;

@Getter
@Setter
@ToString
public class DocumentInfoUpdateDto extends OperationDto {
  
  private String name;
  
  private String description;

}
