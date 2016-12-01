package com.similan.service.api.comment.dto.error;

import com.similan.service.api.base.dto.error.SimilanException;

public class CommentException extends SimilanException {
  private static final long serialVersionUID = 1L;

  public CommentException(CommentErrorCode code, String message, Throwable cause) {
    super(code, message, cause);
  }

  public CommentException(CommentErrorCode code, String message) {
    super(code, message);
  }
}