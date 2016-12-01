package com.similan.service.api.base.dto.error;


public class SimilanForbiddenException extends SimilanException {
  private static final long serialVersionUID = 1L;

  public SimilanForbiddenException(String message, Throwable cause) {
    super(GeneralErrorCode.FORBIDDEN, message, cause);
  }

  public SimilanForbiddenException(String message) {
    super(GeneralErrorCode.FORBIDDEN, message);
  }
}
