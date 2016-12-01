package com.similan.framework.workflow.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkflowInitialRequest extends WorkflowRequest {
  private String flowDefinitionName;
}
