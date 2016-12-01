package com.similan.service.api.feed.dto.error;
import com.similan.service.api.base.dto.error.SimilanException;

public class FeedException extends SimilanException {
  private static final long serialVersionUID = 1L;

  public FeedException(FeedErrorCode code, String message, Throwable cause) {
    super(code, message, cause);
  }

  public FeedException(FeedErrorCode code, String message) {
    super(code, message);
  }
}