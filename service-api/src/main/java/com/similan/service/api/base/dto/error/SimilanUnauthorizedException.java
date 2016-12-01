package com.similan.service.api.base.dto.error;


public class SimilanUnauthorizedException extends SimilanException {
  private static final long serialVersionUID = 1L;

  public SimilanUnauthorizedException(String message, Throwable cause) {
    super(GeneralErrorCode.UNAUTHORIZED, message, cause);
  }

  public SimilanUnauthorizedException(String message) {
    super(GeneralErrorCode.UNAUTHORIZED, message);
  }
}
