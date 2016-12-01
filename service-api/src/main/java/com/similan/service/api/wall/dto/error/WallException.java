package com.similan.service.api.wall.dto.error;

import com.similan.service.api.base.dto.error.SimilanException;

public class WallException extends SimilanException {
  private static final long serialVersionUID = 1L;

  public WallException(WallErrorCode code, String message, Throwable cause) {
    super(code, message, cause);
  }

  public WallException(WallErrorCode code, String message) {
    super(code, message);
  }
}