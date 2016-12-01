package com.similan.service.api.community.dto.error;
import com.similan.service.api.base.dto.error.SimilanException;

public class BusinessException extends SimilanException {
  private static final long serialVersionUID = 1L;

  public BusinessException(BusinessErrorCode code, String message,
      Throwable cause) {
    super(code, message, cause);
  }

  public BusinessException(BusinessErrorCode code, String message) {
    super(code, message);
  }
}