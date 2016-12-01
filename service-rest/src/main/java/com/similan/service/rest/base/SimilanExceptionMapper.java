package com.similan.service.rest.base;

import javax.ws.rs.ext.ExceptionMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import com.similan.service.api.base.dto.error.ErrorDto;
import com.similan.service.api.base.dto.error.SimilanException;

@Component
@Slf4j
public class SimilanExceptionMapper extends
    ExceptionMapperSupport<SimilanException> implements
    ExceptionMapper<SimilanException> {
  @Override
  protected ErrorDto toErrorDto(SimilanException exception) {
    log.info("Similan service exception: " + exception, exception);
    return new ErrorDto(exception.getCode(), exception.getError());
  }
}