package com.similan.portal.view.account;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.service.MemberService;
import com.similan.portal.view.BaseView;
import com.similan.service.exception.CoreServiceException;

@Scope("view")
@Component("forgotPasswordView")
@Slf4j
public class ForgotPasswordView extends BaseView {

  private static final long serialVersionUID = 1L;


  @Autowired(required = false)
  private MemberInfoDto member = null;

  private String pid = null;

  private String memberId = null;

  private String forgotPasswordEmail;

  @PostConstruct
  public void init() {
    memberId = this.getContextParam("mid");
    pid = getContextParam("pid");
  }

  public void setMemberService(MemberService memberService) {
    this.memberService = memberService;
  }

  public MemberInfoDto getMember() {
    if (member == null) {
      member = this.memberService.processForgotPasswordLink(
          Long.valueOf(memberId), this.pid);
    }
    return member;
  }

  public void setMember(MemberInfoDto member) {
    this.member = member;
  }

  public String getPid() {
    return pid;
  }

  public void setPid(String pid) {
    this.pid = pid;
  }

  public String getMemberId() {
    return memberId;
  }

  public void setMemberId(String memberId) {
    this.memberId = memberId;
  }

  public String getForgotPasswordEmail() {
    return forgotPasswordEmail;
  }

  public void setForgotPasswordEmail(String forgotPasswordEmail) {
    this.forgotPasswordEmail = forgotPasswordEmail;
  }

  public void forgotPasswordInitiate() {

    try {

      log.info("Retrieving password for email " + forgotPasswordEmail);
      this.getMemberService().forgotPasswordInitiate(forgotPasswordEmail);
      facesHelper.redirect("/about/forgotPasswordProcessInitiated.xhtml");

    } catch (CoreServiceException exp) {

      log.error("Cannot retrieve password ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
              "Member with email " + forgotPasswordEmail + " does not exist"));
    } catch (Exception exp) {
      log.error("Cannot retrieve password ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
              "Member with email " + forgotPasswordEmail
                  + " either does not exist or have many instances"));
    }

  }

  public void changePassword() {

    try {
      log.info("Changing password to " + this.member.getNewPassword()
          + " from " + this.member.getPassword());

      this.memberService.changeForgottenPasswordComplete(this.member, this.pid);
      facesHelper.redirect("/about/forgotPasswordChangeComplete.xhtml");

    } catch (Exception exp) {
      log.info("Error changing password ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error changing your password" + exp.getMessage()));

    }
  }
}
