package com.similan.framework.dto.partner;

import java.io.Serializable;

public class ExistingPartnerInviteCompleteDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String adminFirstName;

  private String partnerBusinessName;

  private String emailAddress;

  private String adminLastName;

  private String newAdminPassword;

  private String newAdminConfirmPassword;

  private String processInstanceId;

  private Long adminId;

  public ExistingPartnerInviteCompleteDto(String adminFirstName,
      String partnerBusinessName, String emailAddress, String adminLastName,
      String newAdminPassword, String newAdminConfirmPassword,
      String processInstanceId, Long adminId) {
    this.adminFirstName = adminFirstName;
    this.partnerBusinessName = partnerBusinessName;
    this.emailAddress = emailAddress;
    this.adminLastName = adminLastName;
    this.newAdminPassword = newAdminPassword;
    this.newAdminConfirmPassword = newAdminConfirmPassword;
    this.processInstanceId = processInstanceId;
    this.adminId = adminId;
  }

  public String getAdminFirstName() {
    return adminFirstName;
  }

  public String getPartnerBusinessName() {
    return partnerBusinessName;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public String getAdminLastName() {
    return adminLastName;
  }

  public String getNewAdminPassword() {
    return newAdminPassword;
  }

  public String getNewAdminConfirmPassword() {
    return newAdminConfirmPassword;
  }

  public String getProcessInstanceId() {
    return processInstanceId;
  }

  public Long getAdminId() {
    return adminId;
  }

  @Override
  public String toString() {
    return "ExistingPartnerInviteCompleteDto [adminFirstName=" + adminFirstName
        + ", partnerBusinessName=" + partnerBusinessName + ", emailAddress="
        + emailAddress + ", adminLastName=" + adminLastName
        + ", newAdminPassword=" + newAdminPassword
        + ", newAdminConfirmPassword=" + newAdminConfirmPassword
        + ", processInstanceId=" + processInstanceId + ", adminId=" + adminId
        + "]";
  }

}
