package com.similan.service.api.base.dto.error;


public class InvalidKeyException extends SimilanException {
  private static final long serialVersionUID = 1L;

  public InvalidKeyException(String message, Throwable cause) {
    super(GeneralErrorCode.INVALID_KEY, message, cause);
  }

  public InvalidKeyException(String message) {
    super(GeneralErrorCode.INVALID_KEY, message);
  }
}
