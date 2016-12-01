package com.similan.portal.view.survey;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.poll.PollTemplateDto;
import com.similan.framework.dto.update.PollEventDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("pollSubmissionView")
@Slf4j
public class PollSubmissionView extends BaseView {

  private static final long serialVersionUID = 1L;

  private PollEventDto pollEvent = null;

  private PollTemplateDto pollTemplate = null;

  @Autowired
  private MemberInfoDto memberInfo;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  @PostConstruct
  public void init() {

    String pollEventIdParam = getContextParam("rid");
    log.info("Post init in PollSubmissionView for poll event "
        + pollEventIdParam);

    try {

      if (pollEventIdParam != null) {
        Long pollEventId = Long.valueOf(pollEventIdParam);

        /* get the poll event */
        pollEvent = this.getCampaignManagementService().getPollEvent(
            pollEventId);

        /* get the poll template for this event */
        pollTemplate = this.getCampaignManagementService().getPollTemplateByid(
            pollEvent.getPollid());
      }
    } catch (Exception exp) {
      log.error("Cannot fetch poll template ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Cannot fetch the survey"));
    }
  }

  public PollEventDto getPollEvent() {
    return pollEvent;
  }

  public void setPollEvent(PollEventDto pollEvent) {
    this.pollEvent = pollEvent;
  }

  public PollTemplateDto getPollTemplate() {
    return pollTemplate;
  }

  public void setPollTemplate(PollTemplateDto pollTemplate) {
    this.pollTemplate = pollTemplate;
  }

  public String submitSurvey() {

    try {
      this.getCampaignManagementService().submitPollForEvent(pollTemplate,
          pollEvent, memberInfo);
    } catch (Exception exp) {
      log.info("Error summiting poll ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Cannot submit the survey due to " + exp.getMessage()));
    }

    return "result";
  }

}
