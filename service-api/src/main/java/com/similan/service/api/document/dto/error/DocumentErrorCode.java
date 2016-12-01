package com.similan.service.api.document.dto.error;

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
public enum DocumentErrorCode implements IErrorCode {
  DOCUMENT_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  DUPLICATE_DOCUMENT_NAME(HttpStatus.SC_CONFLICT),
  DOCUMENT_ALREADY_LOCKED(HttpStatus.SC_CONFLICT),
  DOCUMENT_LOCK_NOT_HELD(HttpStatus.SC_FORBIDDEN),
  DOCUMENT_INSTANCE_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  DOCUMENT_LABEL_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  DOCUMENT_CATEGORY_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  DOCUMENT_PAGE_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  DOCUMENT_VIEWER_ITEM_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  DOCUMENT_VIEWER_ITEM_NOT_AN_ATTRIBUTE(HttpStatus.SC_EXPECTATION_FAILED),
  DOCUMENT_NAME_EMPTY(HttpStatus.SC_BAD_REQUEST),
  DOCUMENT_LOCKED(HttpStatus.SC_CONFLICT);

  int httpStatus;

  public ServiceType getService() {
    return ServiceType.DOCUMENT;
  }
}