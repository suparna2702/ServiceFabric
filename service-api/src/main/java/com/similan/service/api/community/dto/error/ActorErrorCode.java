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
public enum ActorErrorCode implements IErrorCode {
  ACTOR_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  EXPERTISE_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  INDUSTRY_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  INVALID_SIGN_UP_PROCESS(HttpStatus.SC_BAD_REQUEST), 
  PENDING_VERIFICATION(HttpStatus.SC_CONFLICT),
  DUPLICATE_MEMBER_EMAIL(HttpStatus.SC_CONFLICT),
  DUPLICATE_BUSINESS_EMAIL(HttpStatus.SC_CONFLICT);

  int httpStatus;

  public ServiceType getService() {
    return ServiceType.MEMBER;
  }
}