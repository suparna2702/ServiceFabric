package com.similan.service.api.base.dto.error;

import com.similan.service.api.base.ServiceType;

public interface IErrorCode {
  ServiceType getService();

  String name();

  int getHttpStatus();
}