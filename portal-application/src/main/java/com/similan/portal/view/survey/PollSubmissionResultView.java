package com.similan.portal.view.survey;

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.poll.PollSubmissionDto;
import com.similan.framework.dto.poll.PollTemplateDto;
import com.similan.framework.dto.update.PollEventDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("pollSubmissionResultView")
@Slf4j
public class PollSubmissionResultView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  private List<PollSubmissionDto> pollSubmissions;

  private List<PollEventDto> pendingEventList;

  private PollTemplateDto pollTemplate;

  private PollSubmissionDto selectedSubmission;

  @PostConstruct
  public void init() {
    String pidParam = getContextParam("pid");
    log.info("Init poll submission view for " + pidParam);

    if (pidParam != null) {
      long pid = Long.valueOf(pidParam);
      pollTemplate = this.getCampaignManagementService().getPollTemplateByid(
          pid);
      pollSubmissions = this.getCampaignManagementService()
          .getPollSubmissionsForPoll(pollTemplate);
      pendingEventList = this.getCampaignManagementService()
          .getPollEventFfromOrganization(orgInfo, pid);
    }
  }

  public List<PollEventDto> getPendingEventList() {
    return pendingEventList;
  }

  public void setPendingEventList(List<PollEventDto> pendingEventList) {
    this.pendingEventList = pendingEventList;
  }

  public PollSubmissionDto getSelectedSubmission() {
    return selectedSubmission;
  }

  public void setSelectedSubmission(PollSubmissionDto selectedSubmission) {
    this.selectedSubmission = selectedSubmission;
  }

  public List<PollSubmissionDto> getPollSubmissions() {
    return pollSubmissions;
  }

  public void setPollSubmissions(List<PollSubmissionDto> pollSubmissions) {
    this.pollSubmissions = pollSubmissions;
  }

  public PollTemplateDto getPollTemplate() {
    return pollTemplate;
  }

  public void setPollTemplate(PollTemplateDto pollTemplate) {
    this.pollTemplate = pollTemplate;
  }

}
