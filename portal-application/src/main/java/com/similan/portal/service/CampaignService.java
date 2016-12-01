package com.similan.portal.service;

import java.util.List;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.poll.PollRunEventDto;
import com.similan.framework.dto.poll.PollRunStatisticsDto;
import com.similan.framework.dto.poll.PollSubmissionDto;
import com.similan.framework.dto.poll.PollTemplateDto;
import com.similan.framework.dto.poll.PollTemplateQuestionDto;
import com.similan.framework.dto.update.PollEventDto;

public interface CampaignService {
  
   public PollRunStatisticsDto getPollRunStatistics(Long pollId);

   public void savePollTemplate(PollTemplateDto templateDto,
      OrganizationDetailInfoDto orgInfo);

   public List<PollTemplateDto> getAllPollTemplates(
      OrganizationDetailInfoDto orgInfo);

   public PollTemplateDto getPollTemplateByid(Long pollId);

   public void runPoll(PollRunEventDto runEvent);

   public List<PollEventDto> getPollEventFromOrganization(
      OrganizationDetailInfoDto orgInfo, Long pollId);

   public List<PollEventDto> getPollEventForOrganization(
      OrganizationDetailInfoDto orgInfo);

   public List<PollEventDto> getPollEventForMember(MemberInfoDto memberInfo);

   public PollEventDto getPollEvent(Long eventId);

   public void submitPollForEvent(PollTemplateDto pollTemplate,
      PollEventDto pollEvent, MemberInfoDto memberInfo);

   public List<PollSubmissionDto> getPollSubmissionsForPoll(
      PollTemplateDto pollTemplate);

   public void deleteQuestionFromPoll(PollTemplateDto pollTemplate,
      PollTemplateQuestionDto questionDeleted);

}
