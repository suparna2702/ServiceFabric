package com.similan.service.api.document.dto.error;
import com.similan.service.api.base.dto.error.SimilanException;

public class DocumentException extends SimilanException {
  private static final long serialVersionUID = 1L;

  public DocumentException(DocumentErrorCode code, String message,
      Throwable cause) {
    super(code, message, cause);
  }

  public DocumentException(DocumentErrorCode code, String message) {
    super(code, message);
  }
}