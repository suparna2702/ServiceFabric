package com.similan.framework.workflow.api;

import java.util.concurrent.locks.Lock;

/**
 * Workflow manager is a wrapper over underlying 
 * workflow framework. The current implementation is based on JBPM 4.4
 * @author suparnap
 *
 */
public interface WorkflowManager {
	
	/**
	 * initiates a work flow
	 * @param request
	 * @return
	 */
  WorkflowResponse initiate(WorkflowInitialRequest request);
	
	/**
	 * Initiates a work flow that has no wait states and is to complete fully after it is initiated.
	 * @param request
	 */
	void initiateWithoutResponse(WorkflowInitialRequest request);
	
	/**
	 * resumes or signals a waiting workflow
	 * @param request
	 * @return
	 */
  WorkflowResponse resume(WorkflowResumeRequest request);

  Object getVariable(String workflowInstanceId, String variable);
}
