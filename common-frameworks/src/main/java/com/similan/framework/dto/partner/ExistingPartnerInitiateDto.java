package com.similan.framework.dto.partner;

import java.io.Serializable;

public class ExistingPartnerInitiateDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String inviteeBusinessName;

  private String inviteeEmail;

  private String inviteeFirstName;

  private String inviteeLastName;

  private Long inviterId;

  private Long partnerProgramId;

  public ExistingPartnerInitiateDto(String inviteeBusinessName,
      String inviteeEmail, String inviteeFirstName, String inviteeLastName,
      Long inviterId, Long partnerProgramId) {
    this.inviteeBusinessName = inviteeBusinessName;
    this.inviteeEmail = inviteeEmail;
    this.inviteeFirstName = inviteeFirstName;
    this.inviteeLastName = inviteeLastName;
    this.inviterId = inviterId;
    this.partnerProgramId = partnerProgramId;

  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public String getInviteeBusinessName() {
    return inviteeBusinessName;
  }

  public String getInviteeEmail() {
    return inviteeEmail;
  }

  public String getInviteeFirstName() {
    return inviteeFirstName;
  }

  public String getInviteeLastName() {
    return inviteeLastName;
  }

  public Long getInviterId() {
    return inviterId;
  }

  public Long getPartnerProgramId() {
    return partnerProgramId;
  }

  @Override
  public String toString() {
    return "ExistingPartnerInitiateDto [inviteeBusinessName="
        + inviteeBusinessName + ", inviteeEmail=" + inviteeEmail
        + ", inviteeFirstName=" + inviteeFirstName + ", inviteeLastName="
        + inviteeLastName + ", inviterId=" + inviterId + ", partnerProgramId="
        + partnerProgramId + "]";
  }

}
