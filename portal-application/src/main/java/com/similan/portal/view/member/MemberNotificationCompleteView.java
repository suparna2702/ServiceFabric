package com.similan.portal.view.member;

import java.io.IOException;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.util.JsfUtils;
import com.similan.portal.service.MemberService;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("memberNotificationCompleteView")
@Slf4j
public class MemberNotificationCompleteView extends BaseView {
  private static final long serialVersionUID = 1L;

  private MemberInfoDto member = null;

  private String businessEntityNotificationMsg;


  private boolean notificationResult = true;

  public boolean isNotificationResult() {
    return notificationResult;
  }

  public void setNotificationResult(boolean notificationResult) {
    this.notificationResult = notificationResult;
  }

  public String getBusinessEntityNotificationMsg() {
    return businessEntityNotificationMsg;
  }

  public void setBusinessEntityNotificationMsg(
      String businessEntityNotificationMsg) {
    this.businessEntityNotificationMsg = businessEntityNotificationMsg;
  }

  public MemberInfoDto getMember() {
    return member;
  }

  public void setMember(MemberInfoDto member) {
    this.member = member;
  }

  @PostConstruct
  public void init() {

  }

  /**
   * 1. Get the member ID and process Instance ID from context 2. Invoke the
   * member service 3. And load the member info
   */
  public void processSignupComplete() {
    String processInstanceId = this.getContextParam("pid");
    log.info("Process instance id : " + processInstanceId);

    this.member = this.memberService.memberSignupComplete(processInstanceId);
    if (this.member == null) {
      this.notificationResult = false;
    }
  }

  public void processEmailChangeComplete() {
    String memberValidationId = this.getContextParam("mid");
    long id = Long.parseLong(memberValidationId);
    String processInstanceId = this.getContextParam("pid");

    this.memberService.changeEmailComplete(id, processInstanceId);
  }

  public void businessEntityApprovalComplete() {
    String memberId = this.getContextParam("mid");
    long id = Long.parseLong(memberId);
    String processInstanceId = this.getContextParam("pid");

    this.memberService.createBusinessEntityNotification(id, processInstanceId,
        true, this.businessEntityNotificationMsg);
  }

  public void partnerProgramApproval() {
    String memberValidationId = this.getContextParam("mid");
    String processInstanceId = this.getContextParam("pid");
    String msId = this.getContextParam("msid");
    log.info("Approving partner program " + memberValidationId
        + " process instance " + processInstanceId + " instance id " + msId);

    this.orgService.partnershipApprovalNotification(Long.parseLong(msId),
        processInstanceId, true, null);
  }

  public void memberOrgAssociationApprovalComplete() {
    String memberValidationId = this.getContextParam("mid");
    long id = Long.valueOf(memberValidationId);
    String processInstanceId = this.getContextParam("pid");

    log.info("Member id " + memberValidationId + " long id " + id
        + " process instance id " + processInstanceId);
    this.memberService.memberOrgAssociationNotification(id, processInstanceId,
        true);
  }

  private void readObject(java.io.ObjectInputStream in) throws IOException,
      ClassNotFoundException {
    in.defaultReadObject();
    this.memberService = (MemberService) JsfUtils
        .findBackingBean("memberService");
  }

  public void existingMemberAcceptInvite() {

    String memberValidationId = this.getContextParam("mid");
    long id = Long.parseLong(memberValidationId);
    String processInstanceId = this.getContextParam("pid");
    log.info("Member id : " + memberValidationId
        + " accepting invite for process instance id : " + processInstanceId);

    this.memberService
        .existingMemberAcceptInviteDecision(id, processInstanceId);
  }

  public void existingMemberRejectInvite() {

    String memberValidationId = this.getContextParam("mid");
    long id = Long.parseLong(memberValidationId);
    String processInstanceId = this.getContextParam("pid");
    log.info("Member id : " + memberValidationId
        + " rejecting invite for process instance id : " + processInstanceId);

    this.memberService
        .existingMemberRejectInviteDecision(id, processInstanceId);
  }

}
