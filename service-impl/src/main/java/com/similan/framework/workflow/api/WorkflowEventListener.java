package com.similan.framework.workflow.api;

public interface WorkflowEventListener {
	
	void handleEvent(WorkflowExecutionEvent event);

}
