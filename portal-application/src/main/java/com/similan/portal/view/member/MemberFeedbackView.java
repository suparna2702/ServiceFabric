package com.similan.portal.view.member;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.feedback.dto.NewErrorReportingDto;

@Scope("view")
@Component("memberFeedbackView")
@Slf4j
public class MemberFeedbackView extends BaseView {

  private static final long serialVersionUID = 1L;


  @Autowired(required = true)
  private MemberInfoDto member = null;

  private String memberFeedback;

  public String getMemberFeedback() {
    return memberFeedback;
  }

  public void setMemberFeedback(String memberFeedback) {
    this.memberFeedback = memberFeedback;
  }

  public void reportProblem() {
    if (StringUtils.isBlank(this.memberFeedback)) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_WARN,
              "Feedback cannot be empty. In order to address your concern please "
                  + " provide a valid feedback", ""));
    } else {

      SocialActorKey reporter = this.getMemberKey(member);
      NewErrorReportingDto problem = new NewErrorReportingDto();
      problem.setMemberFeedback(memberFeedback);
      problem.setReporter(reporter);

      this.memberService.reportProblem(problem);
      log.info("Reported error feedback message successfully by "
          + reporter.getId());

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Thank you for your Feedback. We will " + "getback to you soon",
              ""));

      this.memberFeedback = StringUtils.EMPTY;

    }
  }

  public void postMemberFeedback() {
    if (StringUtils.isBlank(this.memberFeedback)) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_WARN,
              "Feedback cannot be empty. In order to address your concern please "
                  + " provide a valid feedback", ""));
    } else {
      this.memberService.addMemberFeedback(this.member.getId(),
          this.memberFeedback);

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Thank you for your Feedback. We will " + "getback to you soon",
              ""));

      this.memberFeedback = StringUtils.EMPTY;

    }
  }

}
