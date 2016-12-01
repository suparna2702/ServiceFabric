package com.similan.portal.view.survey;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.poll.PollTemplateDto;
import com.similan.framework.dto.update.PollEventDto;
import com.similan.portal.view.BaseView;

@Slf4j
public class PollResponseView extends BaseView {

  private static final long serialVersionUID = 1L;

  private PollEventDto pollEvent;

  private PollTemplateDto pollTemplate;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  @PostConstruct
  public void init() {
    log.info("Initializing poll response view ");

    String pid = this.getContextParam("pid");
    log.info("Poll id to be fetched " + pid);

    if (pid != null) {
      Long pollId = Long.valueOf(pid);
      pollTemplate = this.getCampaignManagementService().getPollTemplateByid(
          pollId);
    }

    String rid = this.getContextParam("rid");
    log.info("Poll event id to be fetched " + rid);

    if (rid != null) {
      Long eventId = Long.valueOf(rid);
      pollEvent = this.getCampaignManagementService().getPollEvent(eventId);
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

  public void submitPoll() {
    log.info("Submit poll response");
  }

}
