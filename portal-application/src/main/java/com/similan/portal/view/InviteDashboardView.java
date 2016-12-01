package com.similan.portal.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.community.MemberInviteInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.partner.PartnerProgramInviteDto;
import com.similan.service.api.audit.dto.basic.AuditEventDto;
import com.similan.service.api.audit.dto.basic.AuditEventType;

@Scope("view")
@Component("inviteDashboardView")
@Slf4j
public class InviteDashboardView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired
  private MemberInfoDto memberInfo;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  private List<PartnerProgramInviteDto> partnerInvites;

  private List<MemberInviteInfoDto> memberInvites;

  private AuditEventDto lastPartnerInviteReminder;

  @PostConstruct
  public void init() {
    log.info("InviteDashboardView initialization");

    partnerInvites = this.getPartnerManagementService()
        .getPartnerInvitesByPartnerProgramBusiness(orgInfo.getId());

    memberInvites = this.getMemberService().findPendingInvitesByInviter(
        memberInfo.getId());

    lastPartnerInviteReminder = this.getAuditEventService().getLatest(
        AuditEventType.PARTNER_REINVITE, this.getOrgKey(orgInfo));
  }

  public List<MemberInviteInfoDto> getMemberInvites() {
    return memberInvites;
  }

  public List<PartnerProgramInviteDto> getPartnerInvites() {
    return partnerInvites;
  }

  public AuditEventDto getLastPartnerInviteReminder() {
    return lastPartnerInviteReminder;
  }

  public void cancelMemberInvite(Long id) {
    log.info("Canceling member invite with Id" + id);
    try {
      this.getMemberService().deleteInviteRequest(id);
    } catch (Exception exp) {
      log.error("Error deleting invite request", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error deleting invite request " + exp.getMessage()));
    }
  }

  public void reInviteMember(Long id) {
    log.info("Sending member invite request with Id" + id);
    try {
      this.getMemberService().reInviteMember(id);
    } catch (Exception exp) {
      log.error("Error sending invite request again", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error sending invite request again " + exp.getMessage()));
    }
  }

  public void sendEmployeeReminder() {
    log.info("Sending employee reinvite");
  }

  public void sendPartnerReminder(Long workflowInstanceId) {
    log.info("Sending partner invite reminder for " + workflowInstanceId);
    try {
      List<PartnerProgramInviteDto> reminderList = new ArrayList<PartnerProgramInviteDto>();
      for (PartnerProgramInviteDto reminder : this.partnerInvites) {
        if (reminder.getWorkflowInstanceId().equals(workflowInstanceId)) {
          reminderList.add(reminder);
          this.getPartnerManagementService().sendPartnerInviteReminder(
              reminderList, this.getMemberKey(memberInfo));
          FacesContext.getCurrentInstance().addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
                  "Successfully sent partner invite reinder"));
          break;
        }
      }

      if (reminderList.size() <= 0) {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                "Error sending reminder. No previous invite was selected"));
      }

    } catch (Exception exp) {
      log.error("Cannot send partner invite reminder ", exp);
      FacesContext.getCurrentInstance()
          .addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                  "Cannot send partner invite reminder due to "
                      + exp.getMessage()));
    }

  }

  public void sendPartnerReminder() {
    log.info("Sending partner invite reminder");
    try {
      this.getPartnerManagementService().sendPartnerInviteReminder(
          partnerInvites, this.getMemberKey(memberInfo));
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "Successfully sent partner invite reinder"));
    } catch (Exception exp) {
      log.error("Cannot send partner invite reminder ", exp);
      FacesContext.getCurrentInstance()
          .addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                  "Cannot send partner invite reminder due to "
                      + exp.getMessage()));
    }

  }

}
