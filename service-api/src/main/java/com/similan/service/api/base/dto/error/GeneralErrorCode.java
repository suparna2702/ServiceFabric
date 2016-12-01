package com.similan.service.api.base.dto.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import org.apache.commons.httpclient.HttpStatus;

import com.similan.service.api.base.ServiceType;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum GeneralErrorCode implements IErrorCode {
  GENERIC(HttpStatus.SC_INTERNAL_SERVER_ERROR),
  INVALID_KEY(HttpStatus.SC_NOT_FOUND),
  UNAUTHORIZED(HttpStatus.SC_UNAUTHORIZED),
  FORBIDDEN(HttpStatus.SC_FORBIDDEN),
  CLIENT_ERROR(HttpStatus.SC_BAD_REQUEST);
  
  int httpStatus;

  public ServiceType getService() {
    return ServiceType.GENERAL;
  }
}