package com.similan.adminapp.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.similan.framework.workflow.WorkflowTransientStateDto;

@ViewScoped
@ManagedBean(name = "businessProcessInstanceAdminView")
public class BusinessProcessInstanceAdminView extends BaseAdminView {
	private static final long serialVersionUID = 1L;
	
	List<WorkflowTransientStateDto> workflowInstances;
	
	@PostConstruct
    public void init() {
		workflowInstances = this.businessProcessService.fetchAllPendingProcessInstance();
	}

	public List<WorkflowTransientStateDto> getWorkflowInstances() {
		return workflowInstances;
	}
	
	public void deleteProcessInstance (int processId) {
		this.businessProcessService.deleteBusinessProcessInstance(processId);
	}

	public void setWorkflowInstances(List<WorkflowTransientStateDto> workflowInstances) {
		this.workflowInstances = workflowInstances;
	}

}
