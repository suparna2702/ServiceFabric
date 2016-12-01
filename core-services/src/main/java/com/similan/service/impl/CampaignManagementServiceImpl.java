package com.similan.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.feed.Feed;
import com.similan.domain.entity.feed.SurveyRequestFeedEntry;
import com.similan.domain.entity.poll.PollAnswer;
import com.similan.domain.entity.poll.PollAnswerChoice;
import com.similan.domain.entity.poll.PollAnswerType;
import com.similan.domain.entity.poll.PollEvent;
import com.similan.domain.entity.poll.PollSubmission;
import com.similan.domain.entity.poll.PollSubmissionType;
import com.similan.domain.entity.poll.PollTemplate;
import com.similan.domain.entity.poll.PollTemplateAnswerChoice;
import com.similan.domain.entity.poll.PollTemplateQuestion;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.feed.FeedEntryRepository;
import com.similan.domain.repository.feed.FeedRepository;
import com.similan.domain.repository.poll.PollAnswerChoiceRepository;
import com.similan.domain.repository.poll.PollAnswerRepository;
import com.similan.domain.repository.poll.PollEventRepository;
import com.similan.domain.repository.poll.PollSubmissionRepository;
import com.similan.domain.repository.poll.PollTemplateAnswerChoiceRepository;
import com.similan.domain.repository.poll.PollTemplateQuestionRepository;
import com.similan.domain.repository.poll.PollTemplateRepository;
import com.similan.domain.repository.template.VelocityTemplateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.poll.PollAnswerChoiceDto;
import com.similan.framework.dto.poll.PollAnswerDto;
import com.similan.framework.dto.poll.PollRunEventDto;
import com.similan.framework.dto.poll.PollRunStatisticsDto;
import com.similan.framework.dto.poll.PollSubmissionDto;
import com.similan.framework.dto.poll.PollTemplateAnswerChoiceDto;
import com.similan.framework.dto.poll.PollTemplateDto;
import com.similan.framework.dto.poll.PollTemplateQuestionDto;
import com.similan.framework.dto.update.PollEventDto;
import com.similan.framework.manager.email.EmailManager;
import com.similan.framework.util.EmailValidator;
import com.similan.service.api.CampaignManagementService;
import com.similan.service.api.MemberManagementService;
import com.similan.service.api.OrganizationManagementService;
import com.similan.service.api.community.SocialActorService;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.basic.SocialActorContactDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.feed.FeedService;
import com.similan.service.api.feed.dto.basic.FeedDto;
import com.similan.service.api.feed.dto.key.FeedKey;
import com.similan.service.exception.CoreServiceException;
import com.similan.service.internal.api.feed.FeedInternalService;

@Slf4j
public class CampaignManagementServiceImpl implements CampaignManagementService {
  @Autowired
  private FeedEntryRepository feedEntryRepository;
  @Autowired
  private FeedRepository feedRepository;
  @Autowired
  private FeedService feedService;
  @Autowired
  private FeedInternalService feedInternalService;
  @Autowired
  private SocialOrganizationRepository organizationRepository;
  @Autowired
  private PollTemplateRepository pollTemplateRepository;
  @Autowired
  private PollTemplateQuestionRepository pollTemplateQuestionRepository;
  @Autowired
  private PollTemplateAnswerChoiceRepository pollTemplateAnswerChoiceRepository;
  @Autowired
  private PollEventRepository pollEventRepository;
  @Autowired
  private PollSubmissionRepository pollSubmissionRepository;
  @Autowired
  private SocialActorRepository socialActorRepository;
  @Autowired
  private PollAnswerRepository pollAnswerRepository;
  @Autowired
  private PollAnswerChoiceRepository pollAnswerChoiceRepository;
  @Autowired
  private VelocityTemplateRepository templateRepository;
  @Autowired
  private PlatformCommonSettings commonSettings;
  @Autowired
  private EmailManager emailManager;
  @Autowired
  private SocialOrganizationRepository socialOrganizationRepository;
  @Autowired
  private MemberManagementService memberManagementService;
  @Autowired
  private OrganizationManagementService organizationManagementService;
  @Autowired
  private SocialActorService socialActorService;
  
  @Transactional
  public PollRunStatisticsDto getPollRunStatistics(Long pollId) {
    PollRunStatisticsDto stat = new PollRunStatisticsDto();
    Long submissionCount = this.pollSubmissionRepository
        .getPollSubmissionEventCount(pollId);
    Long eventCount = this.pollEventRepository.getPollEventCount(pollId);

    stat.setEventCount(eventCount);
    stat.setSubmissionCount(submissionCount);

    return stat;
  }

  @Transactional
  public void savePollTemplate(PollTemplateDto templateDto,
      OrganizationDetailInfoDto orgInfo) {

    /**
     * 1. get the social org
     */
    log.info("saving poll template for org " + orgInfo.getId()
        + " for poll template " + templateDto);

    SocialOrganization owner = this.organizationRepository.findOne(orgInfo
        .getId());
    PollTemplate pollTemplate = null;

    if (templateDto.getId() == Long.MIN_VALUE) {

      log.info("Creating new poll template ");
      pollTemplate = this.pollTemplateRepository.create();
      pollTemplate.setCreateStart(new Date());
      pollTemplate.setPollOwner(owner);
      pollTemplate.setPollFor(owner);

      this.pollTemplateRepository.save(pollTemplate);
      templateDto.setId(pollTemplate.getId());

    } else {

      log.info("fetching poll template " + templateDto.getId());
      pollTemplate = this.pollTemplateRepository.findOne(templateDto.getId());

    }

    /* enter the questions */
    for (PollTemplateQuestionDto question : templateDto.getTemplateQuestion()) {

      if (question.getId() == Long.MIN_VALUE) {

        log.info("Creating question " + question);
        PollTemplateQuestion pollTemplateQuestion = this.pollTemplateQuestionRepository
            .create();
        pollTemplateQuestion.setAnswerType(question.getAnswerType());
        pollTemplateQuestion.setQuestionIndex(question.getQuestionIndex());
        pollTemplateQuestion.setQuestionText(question.getQuestionText());

        /* poll question choice */
        for (PollTemplateAnswerChoiceDto questionChoiceDto : question
            .getTemplateAnswerChoice()) {
          PollTemplateAnswerChoice answerChoice = this.pollTemplateAnswerChoiceRepository
              .create();
          answerChoice.setAnswerChoiceText(questionChoiceDto
              .getAnswerChoiceText());
          answerChoice.setAnswerIndex(questionChoiceDto.getAnswerIndex());
          answerChoice.setChoiceUUID(questionChoiceDto.getChoiceUUID());
          this.pollTemplateAnswerChoiceRepository.save(answerChoice);

          pollTemplateQuestion.getTemplateAnswerChoice().add(answerChoice);
        }

        this.pollTemplateQuestionRepository.save(pollTemplateQuestion);
        pollTemplate.getTemplateQuestion().add(pollTemplateQuestion);

      }
    }

    pollTemplate.setTemplateName(templateDto.getTemplateName());
    pollTemplate.setTemplateDescription(templateDto.getTemplateDescription());

    log.info("Saving poll with name " + pollTemplate.getTemplateName());
    this.pollTemplateRepository.save(pollTemplate);
  }

  @Transactional
  public List<PollTemplateDto> getAllPollTemplates(
      OrganizationDetailInfoDto orgInfo) {
    List<PollTemplateDto> retPollTemplateDtoList = new ArrayList<PollTemplateDto>();

    SocialOrganization owner = this.organizationRepository.findOne(orgInfo
        .getId());
    List<PollTemplate> pollTemplateList = this.pollTemplateRepository
        .findAllByOrganization(owner);

    for (PollTemplate pollTemplate : pollTemplateList) {

      PollTemplateDto pollTemplateDto = this
          .getPollTemplateDtoFromDomain(pollTemplate);
      retPollTemplateDtoList.add(pollTemplateDto);
    }

    return retPollTemplateDtoList;
  }

  @Transactional
  private PollTemplateDto getPollTemplateDtoFromDomain(PollTemplate pollTemplate) {

    PollTemplateDto pollTemplateDto = new PollTemplateDto();
    pollTemplateDto.setId(pollTemplate.getId());
    pollTemplateDto.setCreateStart(pollTemplate.getCreateStart());
    pollTemplateDto.setTemplateDescription(pollTemplate
        .getTemplateDescription());
    pollTemplateDto.setTemplateName(pollTemplate.getTemplateName());
    pollTemplateDto.setEventStatistics(this.getPollRunStatistics(pollTemplate
        .getId()));

    /* add the question */
    for (PollTemplateQuestion pollQuestion : pollTemplate.getTemplateQuestion()) {

      PollTemplateQuestionDto pollQuestionDto = new PollTemplateQuestionDto();
      pollQuestionDto.setId(pollQuestion.getId());
      pollQuestionDto.setQuestionText(pollQuestion.getQuestionText());
      pollQuestionDto.setQuestionIndex(pollQuestion.getQuestionIndex());
      pollQuestionDto.setAnswerType(pollQuestion.getAnswerType());

      /* add question choices */
      for (PollTemplateAnswerChoice answerChoice : pollQuestion
          .getTemplateAnswerChoice()) {

        PollTemplateAnswerChoiceDto answerChoiceDto = new PollTemplateAnswerChoiceDto();
        answerChoiceDto.setId(answerChoice.getId());
        answerChoiceDto.setAnswerIndex(answerChoice.getAnswerIndex());
        answerChoiceDto.setAnswerChoiceText(answerChoice.getAnswerChoiceText());
        answerChoiceDto.setChoiceUUID(answerChoice.getChoiceUUID());

        pollQuestionDto.getTemplateAnswerChoice().add(answerChoiceDto);
      }

      pollTemplateDto.getTemplateQuestion().add(pollQuestionDto);
    }

    return pollTemplateDto;
  }

  @Transactional
  public PollTemplateDto getPollTemplateByid(Long pollId) {

    PollTemplate pollTemplate = this.pollTemplateRepository.findOne(pollId);

    PollTemplateDto pollTemplateDto = this
        .getPollTemplateDtoFromDomain(pollTemplate);
    return pollTemplateDto;
  }

  @Transactional
  public List<PollEventDto> getPollEventFfromOrganization(
      OrganizationDetailInfoDto orgInfo, Long pollId) {

    List<PollEventDto> pollEventListDto = new ArrayList<PollEventDto>();
    SocialActor orgActorFrom = this.socialActorRepository.findOne(orgInfo
        .getId());
    List<PollEvent> pollEventList = this.pollEventRepository.findByFrom(
        orgActorFrom.getId(), pollId);

    for (PollEvent pollEvent : pollEventList) {

      PollEventDto pollEventDto = this
          .getPollEventDtoFromDomainObject(pollEvent);
      pollEventListDto.add(pollEventDto);
    }

    return pollEventListDto;
  }

  @Override
  @Transactional
  public List<PollEventDto> getPollEventForOrganization(
      OrganizationDetailInfoDto orgInfo) {

    List<PollEventDto> pollEventListDto = getPollEventForSocialActor(orgInfo
        .getId());
    return pollEventListDto;
  }

  @Override
  @Transactional
  public List<PollEventDto> getPollEventForMember(MemberInfoDto memberInfo) {

    List<PollEventDto> pollEventListDto = getPollEventForSocialActor(memberInfo
        .getId());
    return pollEventListDto;
  }

  @Transactional
  public List<PollEventDto> getPollEventForSocialActor(Long socialActorId) {

    List<PollEventDto> pollEventListDto = new ArrayList<PollEventDto>();
    SocialActor orgActorFor = this.socialActorRepository.findOne(socialActorId);
    List<PollEvent> pollEventList = this.pollEventRepository
        .findByFor(orgActorFor.getId());

    for (PollEvent pollEvent : pollEventList) {

      PollEventDto pollEventDto = this
          .getPollEventDtoFromDomainObject(pollEvent);
      pollEventListDto.add(pollEventDto);
    }

    return pollEventListDto;
  }

  @Transactional
  public void runPollForPartners(PollRunEventDto pollRunDto)
      throws CoreServiceException {
    log.info("Creating poll events ");

    SocialOrganization socialOrganizationFrom = this.socialOrganizationRepository
        .findOne(pollRunDto.getOrgId());
    SocialActorContactDto[] contactList = pollRunDto.getSelectedContacts();

    for (SocialActorContactDto contact : contactList) {

      log.info("Contact id " + contact.getContact().getId()
          + " contact name " + contact.getContact().getDisplayName());

      SocialActor socialActorFor = this.socialActorRepository.findOne(contact
          .getContact().getId());

      log.info("Social actor for contact id " + socialActorFor.getId());
      PollEvent pollEvent = this.pollEventRepository.create();
      pollEvent.setPollDueDate(pollRunDto.getPollDueDate());
      pollEvent.setPollid(pollRunDto.getPollId());
      pollEvent.setPollRunDescription(pollRunDto.getPollRunDescription());
      pollEvent.setPollRunHeader(pollRunDto.getPollRunHeader());
      pollEvent.setUpdateFor(contact.getContact().getId());
      pollEvent.setUpdateFrom(socialOrganizationFrom.getId());
      pollEvent.setResponded(false);
      pollEvent.setPriority(-1);
      pollEvent.setUpdateMemberFrom(pollRunDto.getMemberFrom());
      pollEvent.setTimeStap(new Date());

      // save poll event
      this.pollEventRepository.save(pollEvent);

      // send feed (Hack for now. ideally should be done through event and FeedService)
      FeedKey feedKey = new FeedKey(contact.getContact().getKey());
      FeedDto feed = this.feedService.get(feedKey);
      Feed feedObj = this.feedRepository.findOne(feed.getKey().getOwner()
          .getName());

      log.info("Feed for owner " + feedObj.getOwner());

      SurveyRequestFeedEntry feedEntry = feedEntryRepository
          .createSurveyRequestFeedEntry(feedObj, pollEvent);

      feedInternalService.post(feedEntry);

      try {
        sendSurveyNotifcationEmail(socialOrganizationFrom, contact, pollEvent);
      } catch (Exception e) {
        log.error("Unable to send email", e);

      }
    }
  }

  @Transactional
  private void sendSurveyNotifcationEmail(
      SocialOrganization socialOrganizationFrom, SocialActorContactDto contact,
      PollEvent pollEvent) throws Exception {

    String toAddress = contact.getContact().getContactEmail();
    if (EmailValidator.validate(toAddress) == true) {

      Map<String, Object> emailInputParams = new HashMap<String, Object>();
      String url = this.commonSettings.getPortalApplcationUrl().getValue();
      StringBuilder urlBuilder = new StringBuilder().append(url)
          .append("survey/surveyResponse.xhtml?rid=").append(pollEvent.getId());

      emailInputParams.put("surveyUrl", urlBuilder.toString());
      emailInputParams.put("fromOrganizationName",
          socialOrganizationFrom.getBusinessName());

      String fromAddress = this.commonSettings.getPlatformEmailSenderAddress()
          .getValue();

      /* send email */
      this.emailManager.send(fromAddress, toAddress,
          "surveyAvailableNotificationEmail.vm", emailInputParams);
    }
  }

  @Transactional
  private PollEventDto getPollEventDtoFromDomainObject(PollEvent pollEvent) {

    PollEventDto pollEventDto = new PollEventDto();
    pollEventDto.setFromId(pollEvent.getUpdateFrom());
    pollEventDto.setId(pollEvent.getId());
    pollEventDto.setPollDueDate(pollEvent.getPollDueDate());
    pollEventDto.setPollid(pollEvent.getPollid());
    pollEventDto.setPollRunDescription(pollEvent.getPollRunDescription());
    pollEventDto.setPollRunHeader(pollEvent.getPollRunHeader());
    pollEventDto.setTimeStap(pollEvent.getTimeStap());

    SocialActorKey actorKey = socialActorService.transitional_getKey(
        pollEvent.getUpdateFor());
    ActorDto org = socialActorService.getActor(
        actorKey);
    pollEventDto.setPollFor(org);

    return pollEventDto;
  }

  @Transactional
  public PollEventDto getPollEvent(Long eventId) {
    PollEvent pollEvent = this.pollEventRepository.findOne(eventId);
    return this.getPollEventDtoFromDomainObject(pollEvent);
  }

  @Override
  @Transactional
  public Long getNumberOfPollEventForOrganization(
      OrganizationDetailInfoDto orgInfo) {
    Long num = this.pollEventRepository.findPollEventCountFor(orgInfo.getId());
    return num;
  }

  @Transactional
  public List<PollSubmissionDto> getPollSubmissionsForPoll(
      PollTemplateDto pollTemplateDto) {

    List<PollSubmissionDto> pollSubmissionDtoList = new ArrayList<PollSubmissionDto>();
    List<PollSubmission> pollSubmissionList = this.pollSubmissionRepository
        .findByPollTemplate(pollTemplateDto.getId());

    /* for each template get all submissions */
    for (PollSubmission submission : pollSubmissionList) {

      PollSubmissionDto submissionDto = new PollSubmissionDto();
      submissionDto.setId(submission.getId());
      submissionDto.setPollTemplateId(submissionDto.getPollTemplateId());
      submissionDto.setTimeStamp(submission.getTimeStamp());

      try {
        MemberInfoDto submitterMemberInfo = memberManagementService
            .memberById(submission.getSubmitter().getId());
        submissionDto.setSubmitterMemberInfo(submitterMemberInfo);
      } catch (Exception exception) {
        throw new CoreServiceException("Unable to find submitting member");
      }

      /* get the answers and associated choices */
      for (PollAnswer answer : submission.getPollAnswers()) {

        /* create a dto */
        PollAnswerDto questionDto = new PollAnswerDto();
        questionDto.setId(answer.getId());
        questionDto.setAnswerType(answer.getAnswerType());
        questionDto.setQuestionId(answer.getQuestionId());
        questionDto.setQuestionText(answer.getQuestionText());

        /* add choices */
        for (PollAnswerChoice choice : answer.getAnswerChoice()) {

          PollAnswerChoiceDto choiceDto = new PollAnswerChoiceDto();
          choiceDto.setAnswerRating(choice.getAnswerRating());
          choiceDto.setAnswerText(choice.getAnswerText());
          choiceDto.setChoiceId(choice.getChoiceId());
          choiceDto.setChoiceText(choice.getChoiceText());
          choiceDto.setId(choice.getId());

          questionDto.getAnswerChoices().add(choiceDto);
        }

        submissionDto.getAnswers().add(questionDto);
      }

      pollSubmissionDtoList.add(submissionDto);
    }

    return pollSubmissionDtoList;

  }

  @Transactional
  public void submitPollForEvent(PollTemplateDto pollTemplateDto,
      PollEventDto pollEventDto, MemberInfoDto memberInfo) {

    log.info("Submitting poll " + pollTemplateDto.getId() + " for event "
        + pollEventDto.getId());

    /* get the poll event and create a poll response */
    SocialActor pollSubmitter = this.socialActorRepository.findOne(memberInfo
        .getId());
    PollEvent pollEvent = this.pollEventRepository
        .findOne(pollEventDto.getId());

    PollSubmission pollSubmission = this.pollSubmissionRepository.create();
    pollSubmission.setPollTemplateId(pollTemplateDto.getId());
    pollSubmission.setPollEventFor(pollEvent);
    pollSubmission.setTimeStamp(new Date());
    pollSubmission.setSubmitterType(PollSubmissionType.SocialActor);
    pollSubmission.setSubmitter(pollSubmitter);

    for (PollTemplateQuestionDto questionDto : pollTemplateDto
        .getTemplateQuestion()) {

      /* create an answer */
      PollAnswer pollAnswer = this.pollAnswerRepository.create();
      pollAnswer.setQuestionText(questionDto.getQuestionText());
      pollAnswer.setAnswerType(questionDto.getAnswerType());

      PollAnswerType answerType = questionDto.getAnswerType();

      if (answerType.equals(PollAnswerType.MultiChoice) == true) {

        for (PollTemplateAnswerChoiceDto answerChoiceDto : questionDto
            .getSelectedAnswerChoice()) {

          PollAnswerChoice answerChoice = this.pollAnswerChoiceRepository
              .create();
          answerChoice.setChoiceId(answerChoiceDto.getId());
          answerChoice.setChoiceText(answerChoiceDto.getAnswerChoiceText());
          this.pollAnswerChoiceRepository.save(answerChoice);
          pollAnswer.getAnswerChoice().add(answerChoice);

        }
      } else if ((answerType.equals(PollAnswerType.SingleChoiceList) == true)
          || (answerType.equals(PollAnswerType.SingleChoiceRadio) == true)) {
        if (questionDto.getSelectedAnswer() != null) {
          PollAnswerChoice answerChoice = this.pollAnswerChoiceRepository
              .create();
          answerChoice.setChoiceId(questionDto.getSelectedAnswer().getId());
          answerChoice.setChoiceText(questionDto.getSelectedAnswer()
              .getAnswerChoiceText());
          this.pollAnswerChoiceRepository.save(answerChoice);
          pollAnswer.getAnswerChoice().add(answerChoice);
        }
      } else if (answerType.equals(PollAnswerType.TextInput) == true) {

        PollAnswerChoice answerChoice = this.pollAnswerChoiceRepository
            .create();
        answerChoice.setAnswerText(questionDto.getAnswerText());
        this.pollAnswerChoiceRepository.save(answerChoice);
        pollAnswer.getAnswerChoice().add(answerChoice);

      } else if (answerType.equals(PollAnswerType.Rating) == true) {

        PollAnswerChoice answerChoice = this.pollAnswerChoiceRepository
            .create();
        answerChoice.setAnswerRating(questionDto.getAnswerRating());
        this.pollAnswerChoiceRepository.save(answerChoice);
        pollAnswer.getAnswerChoice().add(answerChoice);

      } else {
        /* throw exception later */
      }

      this.pollAnswerRepository.save(pollAnswer);
      pollSubmission.getPollAnswers().add(pollAnswer);
    }

    this.pollSubmissionRepository.save(pollSubmission);

    pollEvent.setResponded(true);
    this.pollEventRepository.save(pollEvent);

    try {
      sendSurveyCompletedNotifcationEmail(pollEvent);
    } catch (Exception exception) {
      log.error("Unable to send email ", exception);
      // throw new CoreServiceException("Unable to send email " +
      // exception.getMessage());
    }

    // hide the feed entry
    SurveyRequestFeedEntry feedEntry = this.feedEntryRepository.find(pollEvent);
    feedEntry.setShowAll(false);
    this.feedEntryRepository.save(feedEntry);
  }

  @Transactional
  private void sendSurveyCompletedNotifcationEmail(PollEvent pollEvent)
      throws Exception {

    SocialOrganization pollFromSocialOrganization = this.socialOrganizationRepository
        .findOne(pollEvent.getUpdateFrom());
    PollTemplate pollTemplate = this.pollTemplateRepository.findOne(pollEvent
        .getPollid());

    String toAddress = pollFromSocialOrganization.getPrimaryEmail();
    if (EmailValidator.validate(toAddress) == true) {

      Map<String, Object> emailInputParams = new HashMap<String, Object>();

      if (pollFromSocialOrganization != null) {
        emailInputParams.put("submittedOrganizationName",
            pollFromSocialOrganization.getBusinessName());
      }

      if (pollTemplate != null) {
        emailInputParams.put("surveyName", pollTemplate.getTemplateName());
      }

      String fromAddress = this.commonSettings.getPlatformEmailSenderAddress()
          .getValue();

      /* send email */
      this.emailManager.send(fromAddress, toAddress,
          "surveyCompletedNotificationEmail.vm", emailInputParams);
    }
  }

   @Transactional
  public void deleteQuestionFromPoll(PollTemplateDto pollTemplateDto,
      PollTemplateQuestionDto questionDeleted) {
    /**
     * 1. Get the poll 2. Iterate through answer and delete the one sent
     */
    PollTemplate pollTemplate = this.pollTemplateRepository
        .findOne(pollTemplateDto.getId());
    List<PollTemplateQuestion> newList = new ArrayList<PollTemplateQuestion>();

    for (PollTemplateQuestion templateQuestion : pollTemplate
        .getTemplateQuestion()) {
      // FIXME: Workaround for oneToMany delete issue
      // Add everything but the deleted question
      if (templateQuestion.getId() == questionDeleted.getId()) {
        this.pollTemplateQuestionRepository.delete(templateQuestion.getId());
      } else {
        newList.add(templateQuestion);
      }
    }
    pollTemplate.setTemplateQuestion(newList);
    this.pollTemplateRepository.save(pollTemplate);
  }

}
