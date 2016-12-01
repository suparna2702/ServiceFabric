package com.similan.service.api.managementworkspace.dto.error;

import com.similan.service.api.base.dto.error.SimilanException;

public class ContentWorkspaceException extends SimilanException {
  private static final long serialVersionUID = 1L;

  public ContentWorkspaceException(ContentWorkspaceErrorCode code,
      String message, Throwable cause) {
    super(code, message, cause);
  }

  public ContentWorkspaceException(ContentWorkspaceErrorCode code,
      String message) {
    super(code, message);
  }
}