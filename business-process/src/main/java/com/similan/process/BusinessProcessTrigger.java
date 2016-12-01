package com.similan.process;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.service.internal.api.process.BusinessProcessInternalService;

/*
 * Temporary until I move processes to service-impl
 */
@Component
public class BusinessProcessTrigger {
  @Autowired
  private BusinessProcessInternalService businessProcessInternalService;

  @PostConstruct
  public void deployModified() {
    businessProcessInternalService.deployModified();
  }
}
