package com.similan.service.api.base.dto.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class SimilanException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  IErrorCode code;
  String error;

  public SimilanException(IErrorCode code, String error, Throwable cause) {
    super(getMessage(code, error), cause);
    this.code = code;
    this.error = error;
  }

  public SimilanException(IErrorCode code, String error) {
    super(getMessage(code, error));
    this.code = code;
    this.error = error;
  }

  private static String getMessage(IErrorCode code, String error) {
    return code.getService().name() + ":" + code.name() + "["
        + code.getHttpStatus() + "] error:" + error;
  }
}