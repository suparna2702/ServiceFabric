package com.similan.service.api.community.dto.error;

import com.similan.service.api.base.dto.error.SimilanException;

public class MemberException extends SimilanException {
  private static final long serialVersionUID = 1L;

  public MemberException(MemberErrorCode code, String message, Throwable cause) {
    super(code, message, cause);
  }

  public MemberException(MemberErrorCode code, String message) {
    super(code, message);
  }
}