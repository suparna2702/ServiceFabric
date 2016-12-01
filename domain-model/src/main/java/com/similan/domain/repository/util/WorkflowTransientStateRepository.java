package com.similan.domain.repository.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.entity.util.WorkflowTransientStateType;
import com.similan.domain.repository.util.jpa.JpaWorkflowTransientStateRepository;

@Repository
public class WorkflowTransientStateRepository {
  @Autowired
  private JpaWorkflowTransientStateRepository repository;

  public List<WorkflowTransientState> findAll() {
    return repository.findAll();
  }

  public WorkflowTransientState save(WorkflowTransientState state) {
    return repository.save(state);
  }

  public WorkflowTransientState findByMemberIdAndType(Long id,
      WorkflowTransientStateType stateType) {
    return repository.findByMemberIdAndType(id, stateType);
  }

  public List<WorkflowTransientState> findByType(
      WorkflowTransientStateType stateType) {
    return repository.findByType(stateType);
  }

  public WorkflowTransientState findByMemberIdAndProcessInstanceId(Long id,
      String pid) {
    return repository.findByMemberIdAndProcessInstanceId(id, pid);
  }

  public void delete(WorkflowTransientState state) {
    repository.delete(state);
  }

  public WorkflowTransientState findOne(Long id) {
    return repository.findOne(id);
  }

  public WorkflowTransientState findByProcessInstance(String id) {
    return repository.findByProcessInstance(id);
  }

  public WorkflowTransientState create() {

    return new WorkflowTransientState();

  }

  public WorkflowTransientStateAttribute createAttribute() {

    return new WorkflowTransientStateAttribute();

  }

  public WorkflowTransientStateAttribute createAttribute(String paramName,
      String paramValue, String paramType) {

    WorkflowTransientStateAttribute param = new WorkflowTransientStateAttribute();
    param.setAttributeName(paramName);
    param.setAttributeValue(paramValue);
    param.setAttributeType(paramType);
    return param;

  }

}
