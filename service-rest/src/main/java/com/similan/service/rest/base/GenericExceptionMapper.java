package com.similan.service.rest.base;

import javax.ws.rs.ext.ExceptionMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import com.similan.service.api.base.dto.error.ErrorDto;
import com.similan.service.api.base.dto.error.GeneralErrorCode;

@Component
@Slf4j
public class GenericExceptionMapper extends ExceptionMapperSupport<Exception>
    implements ExceptionMapper<Exception> {
  @Override
  protected ErrorDto toErrorDto(Exception exception) {
    log.error("Generic exception: " + exception, exception);
    return new ErrorDto(GeneralErrorCode.GENERIC, getMessage(exception));
  }
}