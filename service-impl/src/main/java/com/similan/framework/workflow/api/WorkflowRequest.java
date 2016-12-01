package com.similan.framework.workflow.api;

import java.util.Map;

import lombok.Setter;

import lombok.Getter;

/**
 * base class for all workflow requests
 * @author suparnap
 *
 */
@Getter
@Setter
public abstract class WorkflowRequest {

  protected String workflowInstanceId;
	protected Map<String,Object> executionParameters;
}
