package com.similan.service.api.feed.dto.error;

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
public enum FeedErrorCode implements IErrorCode {
  FEED_NOT_FOUND(HttpStatus.SC_NOT_FOUND),
  FEED_ENTRY_NOT_FOUND(HttpStatus.SC_NOT_FOUND);

  int httpStatus;

  public ServiceType getService() {
    return ServiceType.FEED;
  }
}