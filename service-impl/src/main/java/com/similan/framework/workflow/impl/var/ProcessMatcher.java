package com.similan.framework.workflow.impl.var;

import org.jbpm.pvm.internal.type.Matcher;

import com.similan.service.internal.impl.process.BusinessProcessActions;

public class ProcessMatcher implements Matcher {
  private static final long serialVersionUID = 1L;

  public boolean matches(String name, Object value) {
    return value instanceof BusinessProcessActions;
  }
  
  @Override
  public String toString() {
    return "process-id";
  }
}
