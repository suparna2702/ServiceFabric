package com.similan.service.api.bookmark.dto.error;

import com.similan.service.api.base.dto.error.SimilanException;

public class BookmarkException extends SimilanException {
  private static final long serialVersionUID = 1L;

  public BookmarkException(BookmarkErrorCode code, String message,
      Throwable cause) {
    super(code, message, cause);
  }

  public BookmarkException(BookmarkErrorCode code, String message) {
    super(code, message);
  }
}