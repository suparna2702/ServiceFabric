package com.similan.framework.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.similan.domain.entity.util.WorkflowTransientStateType;

public class WorkflowTransientStateDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private Long memberId;

  private WorkflowTransientStateType stateType = WorkflowTransientStateType.MemberInvite;

  private String processInstanceId;

  private Date timeStamp;

  private List<WorkflowTransientStateAttributeDto> attributes = new ArrayList<WorkflowTransientStateAttributeDto>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getMemberId() {
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }

  public WorkflowTransientStateType getStateType() {
    return stateType;
  }

  public void setStateType(WorkflowTransientStateType stateType) {
    this.stateType = stateType;
  }

  public String getProcessInstanceId() {
    return processInstanceId;
  }

  public void setProcessInstanceId(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }

  public Date getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(Date timeStamp) {
    this.timeStamp = timeStamp;
  }

  public List<WorkflowTransientStateAttributeDto> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<WorkflowTransientStateAttributeDto> attributes) {
    this.attributes = attributes;
  }

  public WorkflowTransientStateAttributeDto getAttributeByName(String name) {
    for (WorkflowTransientStateAttributeDto attr : this.attributes) {
      if (attr.getAttributeName().equalsIgnoreCase(name)) {
        return attr;
      }
    }

    return null;
  }

  @Override
  public String toString() {
    return "WorkflowTransientStateDto [id=" + id + ", memberId=" + memberId
        + ", stateType=" + stateType + ", processInstanceId="
        + processInstanceId + ", timeStamp=" + timeStamp + ", attributes="
        + attributes + "]";
  }

}
