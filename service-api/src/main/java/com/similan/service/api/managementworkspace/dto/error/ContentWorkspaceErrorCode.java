package com.similan.service.api.managementworkspace.dto.error;

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
public enum ContentWorkspaceErrorCode implements IErrorCode {
  WORKSPACE_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  DUPLICATE_WORKSPACE_NAME(HttpStatus.SC_CONFLICT),
  ALREADY_A_PARTICIPANT(HttpStatus.SC_CONFLICT),
  DOCUMENT_IS_SHARED(HttpStatus.SC_CONFLICT),
  DOCUMENT_IS_SHARED_EXTERNALLY(HttpStatus.SC_CONFLICT),
  DUPLICATE_DOCUMENT_NAME(HttpStatus.SC_CONFLICT);

  int httpStatus;

  public ServiceType getService() {
    return ServiceType.CONTENT_WORKSPACE;
  }
}