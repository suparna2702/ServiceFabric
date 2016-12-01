package com.similan.process.common;

import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.api.model.OpenExecution;
import org.springframework.stereotype.Component;

@Component
public class Engine {
  private static enum ActivityBehaviourAdapter implements ActivityBehaviour {
    INSTANCE;

    @Override
    public void execute(ActivityExecution execution) throws Exception {
    }
  }

  @Value
  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class DecisionHandlerAdapter implements DecisionHandler {
    private static final long serialVersionUID = 1L;

    String value;

    @Override
    public String decide(OpenExecution execution) {
      return value;
    }

  }

  public ActivityBehaviour execute(Object value) {
    return ActivityBehaviourAdapter.INSTANCE;
  }

  public DecisionHandlerAdapter decide(Object value) {
    return new DecisionHandlerAdapter(toString(value));
  }

  private String toString(Object value) {
    if (value == null) {
      return "null";
    }
    if (value instanceof Enum) {
      return ((Enum<?>) value).name();
    }
    return value.toString();
  }
}