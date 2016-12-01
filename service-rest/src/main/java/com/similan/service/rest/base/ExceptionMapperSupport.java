package com.similan.service.rest.base;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.similan.service.api.base.dto.error.ErrorDto;

public abstract class ExceptionMapperSupport<E extends Throwable> {
  public Response toResponse(E exception) {
    ErrorDto error = toErrorDto(exception);
    return Response.status(error.getCode().getHttpStatus())
        .type(MediaType.APPLICATION_JSON).entity(error).build();
  }

  protected String getMessage(E exception) {
    String exceptionName = exception.getClass().getName();
    String exceptionMessage = exception.getMessage();
    return exceptionMessage == null ? exceptionName : exceptionName + ": "
        + exceptionMessage;
  }

  protected abstract ErrorDto toErrorDto(E exception);

  protected int toHttpStatus(E exception, ErrorDto error) {
    return error.getCode().getHttpStatus();
  }

}