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
public enum MemberErrorCode implements IErrorCode {
  MEMBER_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  INTEREST_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  PROFESSIONAL_TITLE_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  INVALID_VERIFICATION_TOKEN(HttpStatus.SC_FORBIDDEN);

  int httpStatus;

  public ServiceType getService() {
    return ServiceType.MEMBER;
  }
}