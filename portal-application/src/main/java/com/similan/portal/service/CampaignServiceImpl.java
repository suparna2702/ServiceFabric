package com.similan.portal.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.poll.PollRunEventDto;
import com.similan.framework.dto.poll.PollRunStatisticsDto;
import com.similan.framework.dto.poll.PollSubmissionDto;
import com.similan.framework.dto.poll.PollTemplateDto;
import com.similan.framework.dto.poll.PollTemplateQuestionDto;
import com.similan.framework.dto.update.PollEventDto;
import com.similan.portal.service.listener.PollRunEvent;
import com.similan.service.impl.CampaignManagementServiceImpl;

@Service("campaignService")
@Slf4j
public class CampaignServiceImpl extends BaseService implements CampaignService {

  private static final long serialVersionUID = 1L;

  @Autowired
  private CampaignManagementServiceImpl campaignService;

  /**
   * Spring event publisher.
   */
  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Transactional
  public void savePollTemplate(PollTemplateDto templateDto,
      OrganizationDetailInfoDto orgInfo) {
    log.info("saving template ");
    campaignService.savePollTemplate(templateDto, orgInfo);
  }

  @Transactional
  public List<PollTemplateDto> getAllPollTemplates(
      OrganizationDetailInfoDto orgInfo) {
    return campaignService.getAllPollTemplates(orgInfo);
  }

  @Transactional
  public PollTemplateDto getPollTemplateByid(Long pollId) {
    return campaignService.getPollTemplateByid(pollId);
  }

  public void runPoll(PollRunEventDto runEvent) {

    PollRunEvent pollRunEvent = new PollRunEvent(runEvent);
    this.eventPublisher.publishEvent(pollRunEvent);

  }

  @Transactional
  public List<PollEventDto> getPollEventForOrganization(
      OrganizationDetailInfoDto orgInfo) {
    return this.campaignService.getPollEventForOrganization(orgInfo);
  }

  @Transactional
  public List<PollEventDto> getPollEventForMember(MemberInfoDto memberInfo) {
    return this.campaignService.getPollEventForMember(memberInfo);
  }

  @Transactional
  public PollEventDto getPollEvent(Long eventId) {
    return this.campaignService.getPollEvent(eventId);
  }

  @Transactional
  public void submitPollForEvent(PollTemplateDto pollTemplate,
      PollEventDto pollEvent, MemberInfoDto memberInfo) {
    this.campaignService
        .submitPollForEvent(pollTemplate, pollEvent, memberInfo);

  }

   @Transactional
  public List<PollSubmissionDto> getPollSubmissionsForPoll(
      PollTemplateDto pollTemplate) {
    return this.campaignService.getPollSubmissionsForPoll(pollTemplate);
  }

  @Transactional
  public void deleteQuestionFromPoll(PollTemplateDto pollTemplate,
      PollTemplateQuestionDto questionDeleted) {
    this.campaignService.deleteQuestionFromPoll(pollTemplate, questionDeleted);

  }

  @Transactional
  public List<PollEventDto> getPollEventFromOrganization(
      OrganizationDetailInfoDto orgInfo, Long pollId) {
    return this.campaignService.getPollEventFfromOrganization(orgInfo, pollId);
  }

  @Override
  @Transactional
  public PollRunStatisticsDto getPollRunStatistics(Long pollId) {
    return this.campaignService.getPollRunStatistics(pollId);
  }

}
