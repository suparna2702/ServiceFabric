package com.similan.service.api.asset.dto.error;

import com.similan.service.api.base.dto.error.SimilanException;

public class AssetException extends SimilanException {
  private static final long serialVersionUID = 1L;

  public AssetException(AssetErrorCode code, String message,
      Throwable cause) {
    super(code, message, cause);
  }

  public AssetException(AssetErrorCode code, String message) {
    super(code, message);
  }
}