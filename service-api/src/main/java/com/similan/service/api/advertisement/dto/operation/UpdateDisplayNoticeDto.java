package com.similan.service.api.advertisement.dto.operation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.service.api.base.dto.operation.OperationDto;

@Getter
@Setter
@ToString
public class UpdateDisplayNoticeDto extends OperationDto {

  private String name;

  private String iconAsset;

  private String text;

}
