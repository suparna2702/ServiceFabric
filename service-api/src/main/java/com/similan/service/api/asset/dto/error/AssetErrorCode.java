package com.similan.service.api.asset.dto.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import org.apache.commons.httpclient.HttpStatus;

import com.similan.service.api.base.ServiceType;
import com.similan.service.api.base.dto.error.IErrorCode;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum AssetErrorCode implements IErrorCode {
  ASSET_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  METADATA_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  ADDITIONAL_METADATA_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  ERROR_UPLOADING_ASSET(HttpStatus.SC_INTERNAL_SERVER_ERROR),
  ERROR_STORING_ASSET(HttpStatus.SC_INTERNAL_SERVER_ERROR),
  ERROR_DETECTING_ASSET(HttpStatus.SC_NOT_ACCEPTABLE),
  ERROR_PROCESSING_ASSET(HttpStatus.SC_INTERNAL_SERVER_ERROR),
  ERROR_RETRIEVING_ASSET(HttpStatus.SC_INTERNAL_SERVER_ERROR),
  ERROR_DELETING_ASSET(HttpStatus.SC_INTERNAL_SERVER_ERROR);

  int httpStatus;

  public ServiceType getService() {
    return ServiceType.ASSET;
  }
}