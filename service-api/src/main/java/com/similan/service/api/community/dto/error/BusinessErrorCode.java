package com.similan.service.api.community.dto.error;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import org.apache.commons.httpclient.HttpStatus;

import com.similan.service.api.base.ServiceType;
import com.similan.service.api.base.dto.error.IErrorCode;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum BusinessErrorCode implements IErrorCode {
  BUSINESS_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  DUPLICATE_BUSINESS_NAME(HttpStatus.SC_CONFLICT),
  NOT_A_PARTNER_PROGRAM(HttpStatus.SC_BAD_REQUEST),
  BUSINESS_ALREADY_INVITED(HttpStatus.SC_BAD_REQUEST);

  int httpStatus;

  public ServiceType getService() {
    return ServiceType.BUSINESS;
  }
}