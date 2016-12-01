package com.similan.service.api.community.dto.error;

import com.similan.service.api.base.dto.error.SimilanException;

public class ActorException extends SimilanException {
  private static final long serialVersionUID = 1L;

  public ActorException(ActorErrorCode code, String message, Throwable cause) {
    super(code, message, cause);
  }

  public ActorException(ActorErrorCode code, String message) {
    super(code, message);
  }
}