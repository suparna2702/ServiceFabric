package com.similan.service.api.advertisement.dto.error;
import com.similan.service.api.base.dto.error.SimilanException;

public class DisplayNoticeException extends SimilanException {
  private static final long serialVersionUID = 1L;

  public DisplayNoticeException(DisplayNoticeErrorCode code, String message,
      Throwable cause) {
    super(code, message, cause);
  }

  public DisplayNoticeException(DisplayNoticeErrorCode code, String message) {
    super(code, message);
  }
}