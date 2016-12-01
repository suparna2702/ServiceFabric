package com.similan.portal.view.partner;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.partner.ExistingPartnerInviteCompleteDto;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.framework.workflow.WorkflowTransientStateDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("existingPartnerInviteInputView")
@Slf4j
public class ExistingPartnerInviteInputView extends BaseView {

  private static final long serialVersionUID = 1L;

  private String adminFirstName;

  private String partnerBusinessName;

  private String emailAddress;

  private String adminLastName;

  private String newAdminPassword;

  private String newAdminConfirmPassword;

  private String processInstanceId;

  private Long adminId;

  private String businessId;

  @PostConstruct
  public void init() {

    this.processInstanceId = this.getContextParam("pid");
    String mid = this.getContextParam("mid");
    this.adminId = Long.valueOf(mid);

    log.info("Initializing existing partner view for process instance "
        + this.processInstanceId + " member admin id " + this.adminId);

    WorkflowTransientStateDto workflowState = this
        .getBusinessProcessManagementService().fetchByProcessInstance(
            this.processInstanceId);

    this.emailAddress = workflowState.getAttributeByName(
        ProcessContextParameterConstants.INVITEE_EMAIL).getAttributeValue();

    this.adminFirstName = workflowState.getAttributeByName(
        ProcessContextParameterConstants.INVITEE_FIRSTNAME).getAttributeValue();

    this.adminLastName = workflowState.getAttributeByName(
        ProcessContextParameterConstants.INVITEE_LASTNAME).getAttributeValue();

    this.partnerBusinessName = workflowState.getAttributeByName(
        ProcessContextParameterConstants.ORG_INVITEE_NAME).getAttributeValue();

    this.businessId = workflowState.getAttributeByName(
        ProcessContextParameterConstants.ORGANIZATION_ID).getAttributeValue();

    log.info("Fetching states from workflow instance invitee first name "
        + this.adminFirstName + " invitee last name " + this.adminLastName
        + " email " + this.emailAddress + " business name "
        + this.partnerBusinessName + " business id " + this.businessId);
  }

  public String getBusinessId() {
    return businessId;
  }

  public String getProcessInstanceId() {
    return processInstanceId;
  }

  public void setProcessInstanceId(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }

  public Long getAdminId() {
    return adminId;
  }

  public void setAdminId(Long adminId) {
    this.adminId = adminId;
  }

  public String getAdminFirstName() {
    return adminFirstName;
  }

  public void setAdminFirstName(String adminFirstName) {
    this.adminFirstName = adminFirstName;
  }

  public String getPartnerBusinessName() {
    return partnerBusinessName;
  }

  public void setPartnerBusinessName(String partnerBusinessName) {
    this.partnerBusinessName = partnerBusinessName;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getAdminLastName() {
    return adminLastName;
  }

  public void setAdminLastName(String adminLastName) {
    this.adminLastName = adminLastName;
  }

  public String getNewAdminPassword() {
    return newAdminPassword;
  }

  public void setNewAdminPassword(String newAdminPassword) {
    this.newAdminPassword = newAdminPassword;
  }

  public String getNewAdminConfirmPassword() {
    return newAdminConfirmPassword;
  }

  public void setNewAdminConfirmPassword(String newAdminConfirmPassword) {
    this.newAdminConfirmPassword = newAdminConfirmPassword;
  }

  public String inviteComplete() {
    log.info("Invite complete " + this.processInstanceId
        + " member admin id " + this.adminId + " password "
        + this.newAdminPassword + " confirm password "
        + this.newAdminConfirmPassword);
    ExistingPartnerInviteCompleteDto submitDto = new ExistingPartnerInviteCompleteDto(
        adminFirstName, partnerBusinessName, emailAddress, adminLastName,
        newAdminPassword, newAdminConfirmPassword, processInstanceId, adminId);

    try {
      this.getPartnerManagementService().existingPartnerInviteComplete(
          submitDto);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Message",
              "You are now successfully connected"));
    } catch (Exception exp) {
      log.error("Error in submission ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "We cannot complete the partner process due to an error"));
    }

    return "result";

  }

}
