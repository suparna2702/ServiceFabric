package com.similan.service.rest.base;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ext.ExceptionMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import com.similan.service.api.base.dto.error.ErrorDto;
import com.similan.service.api.base.dto.error.GeneralErrorCode;

@Component
@Slf4j
public class ClientErrorExceptionMapper extends
    ExceptionMapperSupport<ClientErrorException> implements
    ExceptionMapper<ClientErrorException> {
  protected ErrorDto toErrorDto(ClientErrorException exception) {
    log.info("Client exception: " + exception, exception);
    GeneralErrorCode code;
    String message;
    if (exception instanceof NotFoundException) {
      code = GeneralErrorCode.INVALID_KEY;
      message = "Invalid key.";
    } else {
      code = GeneralErrorCode.CLIENT_ERROR;
      message = getMessage(exception);
    }
    return new ErrorDto(code, message);
  }

  @Override
  protected int toHttpStatus(ClientErrorException exception, ErrorDto error) {
    return exception.getResponse().getStatus();
  }
}