package com.similan.adminapp.service;

import java.util.List;

import com.similan.framework.workflow.WorkflowTransientStateDto;

public interface BusinessProcessAdminService {
  void deleteBusinessProcessInstance(long processId);

  List<WorkflowTransientStateDto> fetchAllPendingProcessInstance();
}
