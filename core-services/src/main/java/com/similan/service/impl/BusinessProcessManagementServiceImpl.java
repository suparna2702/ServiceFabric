package com.similan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.workflow.WorkflowTransientStateDto;
import com.similan.service.api.BusinessProcessManagementService;
import com.similan.service.marshallers.WorkflowTransientStateMarshaller;

public class BusinessProcessManagementServiceImpl implements
    BusinessProcessManagementService {

  @Autowired
  private WorkflowTransientStateRepository workflowTransientStateRepository;

  @Autowired
  private WorkflowTransientStateMarshaller workflowTransientStateMarshaller;

  @Override
  @Transactional
  public List<WorkflowTransientStateDto> fetchAllPendingProcessInstance() {
    List<WorkflowTransientState> transStateDomain = this.workflowTransientStateRepository
        .findAll();
    List<WorkflowTransientStateDto> transStateListDto = this.workflowTransientStateMarshaller
        .marshallStates(transStateDomain);

    return transStateListDto;
  }

  @Override
  @Transactional
  public void deletePendingProcessInstance(long processId) {

    WorkflowTransientState transState = this.workflowTransientStateRepository
        .findOne(processId);
    if (transState != null)
      this.workflowTransientStateRepository.delete(transState);

  }

  @Override
  @Transactional
  public WorkflowTransientStateDto fetchByProcessInstance(String pid) {
    WorkflowTransientState transState = this.workflowTransientStateRepository
        .findByProcessInstance(pid);
    WorkflowTransientStateDto transStateDto = this.workflowTransientStateMarshaller
        .marshallState(transState);
    return transStateDto;
  }

}
