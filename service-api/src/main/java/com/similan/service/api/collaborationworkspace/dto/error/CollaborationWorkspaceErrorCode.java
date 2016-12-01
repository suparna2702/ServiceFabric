package com.similan.service.api.collaborationworkspace.dto.error;

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
public enum CollaborationWorkspaceErrorCode implements IErrorCode {
  WORKSPACE_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  DUPLICATE_WORKSPACE_NAME(HttpStatus.SC_CONFLICT),
  PARTICIPATION_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  SHARED_DOCUMENT_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  TASK_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  DOCUMENT_ALREADY_SHARED(HttpStatus.SC_CONFLICT),
  INVITE_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  PARTICIPANT_ALREADY_INVITED(HttpStatus.SC_CONFLICT),
  SHARED_DOCUMENT_UNSHARED(HttpStatus.SC_UNPROCESSABLE_ENTITY);

  int httpStatus;

  public ServiceType getService() {
    return ServiceType.COLLABORATION_WORKSPACE;
  }
}