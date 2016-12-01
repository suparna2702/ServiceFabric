package com.similan.service.api;

import java.util.List;

import com.similan.framework.workflow.WorkflowTransientStateDto;

public interface BusinessProcessManagementService {
	
	public List<WorkflowTransientStateDto> fetchAllPendingProcessInstance();
	
	public WorkflowTransientStateDto fetchByProcessInstance(String pid);
	
	public void deletePendingProcessInstance(long processId);

}
