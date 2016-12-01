package com.similan.service.api.collaborationworkspace.dto.error;

import com.similan.service.api.base.dto.error.SimilanException;

public class CollaborationWorkspaceException extends SimilanException {
  private static final long serialVersionUID = 1L;

  public CollaborationWorkspaceException(CollaborationWorkspaceErrorCode code,
      String message, Throwable cause) {
    super(code, message, cause);
  }

  public CollaborationWorkspaceException(CollaborationWorkspaceErrorCode code,
      String message) {
    super(code, message);
  }
}