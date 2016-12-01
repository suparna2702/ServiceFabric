package com.similan.service.api.base.dto.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import com.similan.service.api.base.ServiceType;
import com.similan.service.api.base.dto.basic.Dto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level=AccessLevel.PRIVATE)
public class ErrorDto extends Dto {
  public ServiceType getService() {
    return code.getService();
  }
  IErrorCode code;
  String message;
}