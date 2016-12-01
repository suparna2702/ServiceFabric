package com.similan.portal.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class SimilanExceptionHandlerFactory extends ExceptionHandlerFactory {
  private ExceptionHandlerFactory parent;

  public SimilanExceptionHandlerFactory(ExceptionHandlerFactory parent) {
    this.parent = parent;
  }

  @Override
  public ExceptionHandler getExceptionHandler() {
    ExceptionHandler parentHandler = parent.getExceptionHandler();
    ExceptionHandler handler = new SimilanExceptionHandler(parentHandler);
    return handler;
  }
}
