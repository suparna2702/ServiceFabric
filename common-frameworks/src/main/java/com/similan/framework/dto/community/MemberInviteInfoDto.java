package com.similan.framework.dto.community;

import java.io.Serializable;
import java.util.Date;

public class MemberInviteInfoDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String inviteeEmail;

  private Long workflowInstanceId;

  private Date timeStamp;

  private String inviteeFirstName;

  private String inviteeLastName;

  public String getInviteeFirstName() {
    return inviteeFirstName;
  }

  public void setInviteeFirstName(String inviteeFirstName) {
    this.inviteeFirstName = inviteeFirstName;
  }

  public String getInviteeLastName() {
    return inviteeLastName;
  }

  public void setInviteeLastName(String inviteeLastName) {
    this.inviteeLastName = inviteeLastName;
  }

  public String getInviteeEmail() {
    return inviteeEmail;
  }

  public void setInviteeEmail(String inviteeEmail) {
    this.inviteeEmail = inviteeEmail;
  }

  public Long getWorkflowInstanceId() {
    return workflowInstanceId;
  }

  public void setWorkflowInstanceId(Long workflowInstanceId) {
    this.workflowInstanceId = workflowInstanceId;
  }

  public Date getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(Date timeStamp) {
    this.timeStamp = timeStamp;
  }

  @Override
  public String toString() {
    return "MemberInviteInfoDto [inviteeEmail=" + inviteeEmail
        + ", workflowInstanceId=" + workflowInstanceId + ", timeStamp="
        + timeStamp + "]";
  }

}
