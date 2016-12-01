package com.similan.adminapp.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.framework.workflow.WorkflowTransientStateDto;
import com.similan.service.api.BusinessProcessManagementService;

@Service("bpmnAdminService")
public class BusinessProcessAdminServiceImpl implements BusinessProcessAdminService, Serializable {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private BusinessProcessManagementService businessProcessManagementService;
	
	@Transactional
	public List<WorkflowTransientStateDto> fetchAllPendingProcessInstance() {
		return businessProcessManagementService.fetchAllPendingProcessInstance();
	}
	
	@Transactional
	public void deleteBusinessProcessInstance(long processId){
		businessProcessManagementService.deletePendingProcessInstance(processId);
	}
}
