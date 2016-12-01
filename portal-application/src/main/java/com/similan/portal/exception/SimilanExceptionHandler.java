package com.similan.portal.exception;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import lombok.extern.slf4j.Slf4j;

import com.sun.faces.context.FacesFileNotFoundException;

@Slf4j
public class SimilanExceptionHandler extends ExceptionHandlerWrapper {
  private ExceptionHandler wrapped;

  public SimilanExceptionHandler(ExceptionHandler wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public ExceptionHandler getWrapped() {
    return wrapped;
  }

  @Override
  public void handle() throws FacesException {
    Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents()
        .iterator();
    while (i.hasNext()) {
      ExceptionQueuedEvent event = i.next();
      ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event
          .getSource();
      Throwable t = context.getException();
      if (!(t instanceof FacesFileNotFoundException)) {
        log.error("Application error: " + t, t);
      }
    }
    getWrapped().handle();
  }
}
