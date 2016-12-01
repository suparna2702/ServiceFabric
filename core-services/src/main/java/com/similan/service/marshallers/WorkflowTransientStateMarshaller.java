package com.similan.service.marshallers;

import java.util.ArrayList;
import java.util.List;

import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.framework.workflow.WorkflowTransientStateAttributeDto;
import com.similan.framework.workflow.WorkflowTransientStateDto;

public class WorkflowTransientStateMarshaller {

  public WorkflowTransientStateDto marshallState(
      WorkflowTransientState transState) {

    WorkflowTransientStateDto transStateDto = new WorkflowTransientStateDto();
    transStateDto.setId(transState.getId());
    transStateDto.setMemberId(transState.getMemberId());
    transStateDto.setProcessInstanceId(transState.getProcessInstanceId());
    transStateDto.setStateType(transState.getStateType());

    List<WorkflowTransientStateAttribute> transAttrList = transState
        .getAttributes();
    for (WorkflowTransientStateAttribute transAttribute : transAttrList) {
      WorkflowTransientStateAttributeDto transAttrDto = new WorkflowTransientStateAttributeDto();
      transAttrDto.setId(transAttribute.getId());
      transAttrDto.setAttributeType(transAttribute.getAttributeType());
      transAttrDto.setAttributeValue(transAttribute.getAttributeValue());
      transAttrDto.setAttributeName(transAttribute.getAttributeName());
      transStateDto.getAttributes().add(transAttrDto);
    }

    return transStateDto;

  }

  public List<WorkflowTransientStateDto> marshallStates(
      List<WorkflowTransientState> transStateList) {
    List<WorkflowTransientStateDto> transStateListDto = new ArrayList<WorkflowTransientStateDto>();
    for (WorkflowTransientState transState : transStateList) {
      WorkflowTransientStateDto stateDto = this.marshallState(transState);
      transStateListDto.add(stateDto);
    }

    return transStateListDto;
  }

}
