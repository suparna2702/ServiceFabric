package com.similan.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceParticipation;
import com.similan.domain.entity.community.Account;
import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialContact;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.partner.PartnerPreQualificationAnswer;
import com.similan.domain.entity.partner.PartnerPreQualificationAnswerChoice;
import com.similan.domain.entity.partner.PartnerPreQualificationAnswerType;
import com.similan.domain.entity.partner.PartnerPreQualificationQuestion;
import com.similan.domain.entity.partner.PartnerPreQualificationQuestionResponse;
import com.similan.domain.entity.partner.PartnerProgramBenifit;
import com.similan.domain.entity.partner.PartnerProgramDefinition;
import com.similan.domain.entity.partner.PartnerProgramRequiredAttribute;
import com.similan.domain.entity.partner.PartnerProgramRequiredAttributeType;
import com.similan.domain.entity.partner.PartnerProgramResponse;
import com.similan.domain.entity.partner.PartnerRelationshipCategory;
import com.similan.domain.entity.partner.Partnership;
import com.similan.domain.entity.partner.PartnershipStatus;
import com.similan.domain.entity.process.BusinessProcessDefinition;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.entity.util.WorkflowTransientStateType;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceParticipationRepository;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceRepository;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.domain.repository.community.SocialContactRepository;
import com.similan.domain.repository.community.SocialEmployeeRepository;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.partner.PartnerPreQualificationAnswerChoiceRepository;
import com.similan.domain.repository.partner.PartnerPreQualificationAnswerRepository;
import com.similan.domain.repository.partner.PartnerPreQualificationQuestionRepository;
import com.similan.domain.repository.partner.PartnerPreQualificationQuestionResponseRepository;
import com.similan.domain.repository.partner.PartnerProgramBenifitRepository;
import com.similan.domain.repository.partner.PartnerProgramCommunicationAttributeRepository;
import com.similan.domain.repository.partner.PartnerProgramDefinitionRepository;
import com.similan.domain.repository.partner.PartnerProgramRequiredAttributeRepository;
import com.similan.domain.repository.partner.PartnerProgramResponseRepository;
import com.similan.domain.repository.partner.PartnerProgramTypeInfoRepository;
import com.similan.domain.repository.partner.PartnerRelationshipCategoryRepository;
import com.similan.domain.repository.partner.PartnershipRepository;
import com.similan.domain.repository.process.BusinessProcessDefinitionRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.partner.ExistingPartnerInitiateDto;
import com.similan.framework.dto.partner.ExistingPartnerInviteCompleteDto;
import com.similan.framework.dto.partner.PartnerPreQualificationAnswerChoiceDto;
import com.similan.framework.dto.partner.PartnerPreQualificationQuestionDto;
import com.similan.framework.dto.partner.PartnerProgramBenifitDto;
import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.framework.dto.partner.PartnerProgramInviteDto;
import com.similan.framework.dto.partner.PartnerRelationshipCategoryDto;
import com.similan.framework.dto.partner.PartnerRequiredAttributeDto;
import com.similan.framework.dto.partner.PartnershipDto;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.framework.workflow.api.WorkflowInitialRequest;
import com.similan.framework.workflow.api.WorkflowManager;
import com.similan.framework.workflow.api.WorkflowResumeRequest;
import com.similan.service.api.PartnerManagementService;
import com.similan.service.api.audit.AuditEventService;
import com.similan.service.api.audit.dto.basic.AuditEventAttributeType;
import com.similan.service.api.audit.dto.basic.AuditEventType;
import com.similan.service.api.audit.dto.operation.NewAuditEventAttributeDto;
import com.similan.service.api.audit.dto.operation.NewAuditEventDto;
import com.similan.service.api.collaborationworkspace.CollaborationWorkspaceService;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.operation.NewCollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewInviteDto;
import com.similan.service.api.community.SocialActorService;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.connection.dto.basic.ContactType;
import com.similan.service.exception.CoreServiceException;
import com.similan.service.exception.PartnerManagementServiceException;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.internal.api.email.EmailInternalService;
import com.similan.service.internal.api.email.EmailParameterConstants;
import com.similan.service.internal.api.email.io.NewEmail;
import com.similan.service.internal.api.event.EventInternalService;
import com.similan.service.internal.impl.security.component.PartnerProgramEnforcer;
import com.similan.service.marshallers.PartnerProgramMarshaller;

@Slf4j
public class PartnerManagementServiceImpl implements PartnerManagementService {

  @Autowired
  private WorkflowTransientStateRepository transientStateRepository;
  @Autowired
  private SocialOrganizationRepository organizationRepository;
  @Autowired
  private SocialPersonRepository memberRepository;
  @Autowired
  private SocialActorMarshaller socialActorMarshaller;
  @Autowired
  private PartnershipRepository partnershipRepository;
  @Autowired
  private SocialActorRepository socialActorRepository;
  @Autowired
  private PartnerProgramDefinitionRepository partnerProgramRepository;
  @Autowired
  private PartnerProgramResponseRepository partnerProgramResponseRepository;
  @Autowired
  private PartnerProgramRequiredAttributeRepository partnerProgramRequiredAttributeRepository;
  @Autowired
  private PartnerPreQualificationQuestionResponseRepository partnerPreQualificationQuestionResponseRepository;
  @Autowired
  private WorkflowManager workflowManager;
  @Autowired
  private BusinessProcessDefinitionRepository businessProcessDefinitionRepository;
  @Autowired
  private PartnerPreQualificationAnswerRepository partnerPreQualificationAnswerRepository;
  @Autowired
  private PartnerPreQualificationAnswerChoiceRepository partnerPreQualificationAnswerChoiceRepository;
  @Autowired
  private PartnerPreQualificationQuestionRepository partnerPreQualificationQuestionRepository;
  @Autowired
  private PartnerProgramTypeInfoRepository partnerProgramTypeInfoRepository;
  @Autowired
  private PartnerProgramBenifitRepository partnerProgramBenifitRepository;
  @Autowired
  private PartnerProgramCommunicationAttributeRepository partnerProgramCommunicationAttributeRepository;
  @Autowired
  private PartnerRelationshipCategoryRepository partnerRelationshipCategoryRepository;
  @Autowired
  private PartnerProgramMarshaller partnerProgramMarshaller;
  @Autowired
  private CollaborationWorkspaceService collaborationWorkspaceService;
  @Autowired
  private CollaborationWorkspaceParticipationRepository collaborationWorkspaceParticipationRepository;
  @Autowired
  private SocialActorService socialActorService;
  @Autowired
  private CollaborationWorkspaceRepository collaborationWorkspaceRepository;
  @Autowired
  private SocialContactRepository socialContactRepository;
  @Autowired
  private PartnerProgramEnforcer partnerProgramEnforcer;
  @Autowired
  private SocialEmployeeRepository socialEmployeeRepository;
  @Autowired
  private EventInternalService eventInternalService;
  @Autowired
  private EmailInternalService emailMessagingService;
  @Autowired
  private PlatformCommonSettings platformCommonSettings;
  @Autowired
  private AuditEventService auditEvetService;

  @Override
  @Transactional(readOnly = true)
  public CollaborationWorkspaceKey getPartnerWorkspace(Long partnerProgramId) {
    PartnerProgramDefinition program = this.partnerProgramRepository
        .findOne(partnerProgramId);
    if (program != null) {
      partnerProgramEnforcer.getWorkspace(program).checkAllowed();
      CollaborationWorkspace workspace = program.getPartnerProgramWorkspace();
      if (workspace != null) {
        CollaborationWorkspaceKey retKey = new CollaborationWorkspaceKey(
            workspace.getOwner().getName(), workspace.getName());
        return retKey;
      }
    }

    return null;
  }

  /**
   * This will return all the workspaces associated with the partner programs
   * where the Business participates
   * 
   * @param orgInfo
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public List<CollaborationWorkspaceKey> getWorkspacesForParticipatingPartnerPrograms(
      OrganizationDetailInfoDto orgInfo, MemberInfoDto memberInfo) {

    SocialPerson invoker = this.memberRepository.findOne(memberInfo.getId());
    SocialOrganization business = this.organizationRepository.findOne(orgInfo
        .getId());

    List<CollaborationWorkspaceKey> workspaceList = new ArrayList<CollaborationWorkspaceKey>();
    partnerProgramEnforcer.getPartnershipsForPartner(business).checkAllowed();

    // get all the partnerships
    List<Partnership> partnershipList = this.partnershipRepository
        .findPartnershipForBusiness(business);

    // find all the partner programs
    for (Partnership partShip : partnershipList) {
      PartnerProgramDefinition partnerProgram = partShip.getPartnerProgram();

      // get the workspace
      CollaborationWorkspace associatedWorkspace = partnerProgram
          .getPartnerProgramWorkspace();

      if (associatedWorkspace != null) {

        CollaborationWorkspaceParticipation participation = collaborationWorkspaceParticipationRepository
            .findByWorkspaceAndParticipant(associatedWorkspace, invoker);
        if (participation != null) {
          // create workspace key
          CollaborationWorkspaceKey workspaceKey = new CollaborationWorkspaceKey(
              associatedWorkspace.getOwner().getName(),
              associatedWorkspace.getName());
          workspaceList.add(workspaceKey);
        }
      }
    }

    return workspaceList;
  }

  @Override
  @Transactional(readOnly = true)
  public CollaborationWorkspaceKey getDefaultWorkspaceForParticipatingPartnerPrograms(
      Long bussId) {

    SocialOrganization business = this.organizationRepository.findOne(bussId);

    // get all the partnerships
    List<Partnership> partnershipList = this.partnershipRepository
        .findPartnershipForBusiness(business);

    // find all the partner programs
    for (Partnership partShip : partnershipList) {
      PartnerProgramDefinition partnerProgram = partShip.getPartnerProgram();
      log.info("Partner program " + partnerProgram.getName() + " owner "
          + partnerProgram.getProgramOwner().getBusinessName());

      // get the workspace
      CollaborationWorkspace associatedWorkspace = partnerProgram
          .getPartnerProgramWorkspace();

      if (associatedWorkspace != null) {
        CollaborationWorkspaceKey workspaceKey = new CollaborationWorkspaceKey(
            associatedWorkspace.getOwner().getName(),
            associatedWorkspace.getName());

        log.info("Returning collab workspace for this partner " + workspaceKey);

        return workspaceKey;
      }
    }

    return null;

  }

  @Transactional(readOnly = true)
  public List<PartnerProgramDefinitionDto> getParticipatingPartnerPrograms(
      SocialOrganization socialOrg) {
    partnerProgramEnforcer.getPartnershipsForPartner(socialOrg).checkAllowed();
    List<Partnership> partnershipList = this.partnershipRepository
        .findPartnershipForBusiness(socialOrg);
    List<PartnerProgramDefinitionDto> partnerProgramList = new ArrayList<PartnerProgramDefinitionDto>();

    for (Partnership partShip : partnershipList) {
      PartnerProgramDefinition partnerProgram = partShip.getPartnerProgram();
      PartnerProgramDefinitionDto program = this.partnerProgramMarshaller
          .marshallPartnerProgramDefinition(partnerProgram);
      partnerProgramList.add(program);
    }

    return partnerProgramList;
  }

  @Transactional(readOnly = true)
  public List<PartnerProgramDefinitionDto> getPartnerProgramsForBusiness(
      OrganizationDetailInfoDto orgInfo) {
    SocialOrganization owner = this.organizationRepository.findOne(orgInfo
        .getId());
    partnerProgramEnforcer.getAll().checkAllowed();
    List<PartnerProgramDefinition> partnerProgramDefList = this.partnerProgramRepository
        .getProgramsForOwner(owner);

    List<PartnerProgramDefinitionDto> partnerProgramList = this.partnerProgramMarshaller
        .marshallPartnerProgramDefinitions(partnerProgramDefList);
    return partnerProgramList;
  }

  @Transactional(readOnly = true)
  public List<PartnerProgramDefinitionDto> getPartnerPrograms() {
    partnerProgramEnforcer.getAll().checkAllowed();

    List<PartnerProgramDefinition> partnerProgramDefList = this.partnerProgramRepository
        .findAll();
    List<PartnerProgramDefinitionDto> partnerProgramList = this.partnerProgramMarshaller
        .marshallPartnerProgramDefinitions(partnerProgramDefList);

    return partnerProgramList;
  }

  @Transactional
  public void partnerProgramApprovalNotification(long msId,
      String processInstanceId, boolean approved, String comment) {

    long programId = (Long) workflowManager.getVariable(processInstanceId,
        ProcessContextParameterConstants.PARTNER_PARTNERSHIP_ID);
    PartnerProgramDefinition program = partnerProgramRepository
        .findOne(programId);
    partnerProgramEnforcer.approve(program).checkAllowed();

    /* set the parameters */
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters
        .put(ProcessContextParameterConstants.MEMBER_TRANS_STATE_ID, msId);
    parameters.put(ProcessContextParameterConstants.PARTNER_PROGRAM_APPROVAL,
        new Boolean(approved));
    parameters.put(ProcessContextParameterConstants.PARTNERSHIP_COMMENT,
        comment);
    log.info("Partner program notification approval status " + approved);

    WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
    resumeRequest.setExecutionParameters(parameters);
    resumeRequest.setWorkflowInstanceId(processInstanceId);
    this.workflowManager.resume(resumeRequest);
  }

  @Transactional(readOnly = true)
  public List<PartnershipDto> getPartnersForProgram(Long programId) {
    PartnerProgramDefinition program = partnerProgramRepository
        .findOne(programId);
    partnerProgramEnforcer.getPartners(program).checkAllowed();

    /**
     * 1. Get all Partnerships 2. Copy to Partnership DTO
     */
    List<Partnership> partnerShipList = this.partnershipRepository
        .findPartnershipByPartnerProgram(programId);
    List<PartnershipDto> retPartnershipList = this.partnerProgramMarshaller
        .marshallPartnerships(partnerShipList);
    return retPartnershipList;
  }

  @Transactional
  private Partnership createPartnershipFromPartnerProgram(Long partOrgId,
      PartnerProgramDefinitionDto selectedPartnerProgram, String approvalComment) {

    SocialOrganization partnerSocialOrg = organizationRepository
        .findOne(partOrgId);
    PartnerProgramDefinition partnerProgram = partnerProgramRepository
        .findOne(selectedPartnerProgram.getId());

    /* create partnership */
    Partnership partnership = partnershipRepository.create(partnerProgram,
        partnerSocialOrg);
    partnership.setPartnershipStatus(PartnershipStatus.Pending);
    partnership.setCreated(new Date());

    /* create the response object */
    PartnerProgramResponse programResponse = this.partnerProgramResponseRepository
        .create();
    programResponse.setTimeStamp(new Date());

    /* create the prequal question response */
    for (PartnerPreQualificationQuestionDto question : selectedPartnerProgram
        .getPreQualificationQuestions()) {

      /* create an answer */
      PartnerPreQualificationAnswer answer = this.partnerPreQualificationAnswerRepository
          .create();
      answer.setQuestionText(question.getQuestionText());
      answer.setAnswerType(question.getAnswerType());

      PartnerPreQualificationAnswerType answerType = question.getAnswerType();

      if (answerType.equals(PartnerPreQualificationAnswerType.MultiChoice) == true) {

        for (PartnerPreQualificationAnswerChoiceDto answerChoiceDto : question
            .getSelectedAnswerChoice()) {
          log.info("Answer to multichoice prequal "
              + answerChoiceDto.getAnswerChoiceText());

          PartnerPreQualificationQuestionResponse answerChoice = this.partnerPreQualificationQuestionResponseRepository
              .create();
          // answerChoice.setId(answerChoiceDto.getId());
          answerChoice.setChoiceId(answerChoiceDto.getId());
          answerChoice.setAnswerText(answerChoiceDto.getAnswerChoiceText());
          this.partnerPreQualificationQuestionResponseRepository
              .save(answerChoice);
          answer.getAnswerChoice().add(answerChoice);
        }
      } else if ((answerType
          .equals(PartnerPreQualificationAnswerType.SingleChoiceList) == true)
          || (answerType
              .equals(PartnerPreQualificationAnswerType.SingleChoiceRadio) == true)) {
        if (question.getSelectedAnswer() != null) {
          log.info("Answer to choice prequal "
              + question.getSelectedAnswer().getAnswerChoiceText());

          PartnerPreQualificationQuestionResponse answerChoice = this.partnerPreQualificationQuestionResponseRepository
              .create();
          // answerChoice.setId(question.getSelectedAnswer().getId());
          answerChoice.setChoiceId(question.getSelectedAnswer().getId());
          answerChoice.setAnswerText(question.getSelectedAnswer()
              .getAnswerChoiceText());
          this.partnerPreQualificationQuestionResponseRepository
              .save(answerChoice);
          answer.getAnswerChoice().add(answerChoice);
        } else
          log.info("Answer type choice but null");

      } else if (answerType.equals(PartnerPreQualificationAnswerType.TextInput) == true) {
        log.info("Answer to text input prequal " + question.getAnswerText());

        PartnerPreQualificationQuestionResponse answerChoice = this.partnerPreQualificationQuestionResponseRepository
            .create();
        answerChoice.setAnswerText(question.getAnswerText());
        this.partnerPreQualificationQuestionResponseRepository
            .save(answerChoice);
        answer.getAnswerChoice().add(answerChoice);

      } else if (answerType.equals(PartnerPreQualificationAnswerType.Rating) == true) {
        log.info("Answer to rating  prequal " + question.getAnswerRating());

        PartnerPreQualificationQuestionResponse answerChoice = this.partnerPreQualificationQuestionResponseRepository
            .create();
        answerChoice.setAnswerRating(question.getAnswerRating());
        this.partnerPreQualificationQuestionResponseRepository
            .save(answerChoice);
        answer.getAnswerChoice().add(answerChoice);

      } else {
        /* throw exception later */
      }

      this.partnerPreQualificationAnswerRepository.save(answer);
      programResponse.getPartnerPreQualificationAnswer().add(answer);
    }

    programResponse = this.partnerProgramResponseRepository
        .save(programResponse);
    partnership.setResponse(programResponse);
    partnership.setComment(approvalComment);
    partnership = partnershipRepository.save(partnership);

    // send the event and email
    List<Partnership> partners = partnershipRepository
        .findPartnershipByPartnerProgram(partnerProgram.getId());
    List<SocialActor> participants = new ArrayList<SocialActor>();
    for (Partnership partner : partners) {
      participants.add(partner.getPartner());
    }

    // send notification email to other partners
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("partnerProgramName", partnerProgram.getName());
    parameters.put("partnerName", partnership.getPartner().getDisplayName());

    NewEmail newEmail = new NewEmail(partnership.getPartner(), participants,
        "partnerProgramJoin.vm", parameters);
    this.emailMessagingService.send(newEmail);

    return partnership;

  }

  @Transactional
  public void participateInPartnerProgram(OrganizationDetailInfoDto partOrg,
      PartnerProgramDefinitionDto selectedPartnerProgram,
      MemberInfoDto memberInfo) {

    PartnerProgramDefinition program = partnerProgramRepository
        .findOne(selectedPartnerProgram.getId());
    partnerProgramEnforcer.join(program).checkAllowed();

    Partnership partnership = createPartnershipFromPartnerProgram(
        partOrg.getId(), selectedPartnerProgram, null);

    /* set the parameters */
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(ProcessContextParameterConstants.ORGANIZATION_ID,
        partOrg.getId());
    parameters.put(ProcessContextParameterConstants.PARTNER_PROGRAM_ID,
        selectedPartnerProgram.getId());
    parameters.put(ProcessContextParameterConstants.MEMBER_ID,
        memberInfo.getId());
    parameters.put(ProcessContextParameterConstants.PARTNER_PARTNERSHIP_ID,
        partnership.getId());

    BusinessProcessDefinition businessCreationProcess = this.businessProcessDefinitionRepository
        .findByName("joinPartnerProgram");
    log.info("business process for joining partner program "
        + businessCreationProcess.getName());

    WorkflowInitialRequest initialRequest = new WorkflowInitialRequest();
    initialRequest.setFlowDefinitionName(businessCreationProcess.getName());

    /* set the parameters */
    initialRequest.setExecutionParameters(parameters);

    /* execute the workflow */
    this.workflowManager.initiate(initialRequest);

  }

  @Transactional(readOnly = true)
  public PartnershipDto getPartnershipDtoById(Long id) {
    Partnership partnership = this.partnershipRepository.findOne(id);
    partnerProgramEnforcer.getPartnership(partnership).checkAllowed();
    return this.partnerProgramMarshaller.marshallPartnership(partnership);
  }

  @Transactional(readOnly = true)
  private PartnershipDto getPartnershipDtoFromDomain(Partnership partnership) {
    return this.partnerProgramMarshaller.marshallPartnership(partnership);
  }

  @Override
  @Transactional
  public void approvePartnerProgramPerticipation(Long partnerProgramId,
      Long partnershipId, Long partnerOrgId) {

    SocialOrganization partnerSocialOrg = this.organizationRepository
        .findOne(partnerOrgId);

    PartnerProgramDefinition partnerProgram = this.partnerProgramRepository
        .findOne(partnerProgramId);
    SocialOrganization partnerProgramOwner = partnerProgram.getProgramOwner();
    Partnership partnership = this.partnershipRepository.findOne(partnershipId);

    /* Now create a partnership */
    partnership.setPartnershipStatus(PartnershipStatus.Approved);
    partnership.setCreated(new Date());
    this.partnershipRepository.save(partnership);

    /* create the business to business relationship */
    SocialContact partnerContact = this.socialContactRepository.create(
        ContactType.ChannelPartner, (SocialActor) partnerProgramOwner,
        (SocialActor) partnerSocialOrg);
    this.socialContactRepository.save(partnerContact);
    this.organizationRepository.save(partnerSocialOrg);

    /* update workspace participation */
    CollaborationWorkspace workspace = partnerProgram
        .getPartnerProgramWorkspace();

    if (workspace != null) {
      CollaborationWorkspaceKey workspaceKey = new CollaborationWorkspaceKey(
          workspace.getOwner().getName(), workspace.getName());

      List<SocialActorKey> participants = new ArrayList<SocialActorKey>();
      participants.add(this.socialActorService
          .transitional_getKey(partnerSocialOrg.getId()));

      // create employee
      Set<EmployeeRole> roles = new HashSet<EmployeeRole>();
      roles.add(EmployeeRole.PARTNER_PROGRAM_ADMIN);

      List<SocialEmployee> partnerAdmins = socialEmployeeRepository
          .findByContactFromAndRolesIn(partnerSocialOrg, roles);
      if (partnerAdmins != null && partnerAdmins.size() > 0) {
        for (SocialEmployee employeePartnerAdmin : partnerAdmins) {
          participants
              .add(this.socialActorService
                  .transitional_getKey(employeePartnerAdmin.getContactTo()
                      .getId()));
        }
      }

      NewInviteDto invite = new NewInviteDto(workspaceKey, participants);
      this.collaborationWorkspaceService
          .addParticipantInPartnerWorkspace(invite);
    }

  }

  @Transactional
  public void rejectPartnerProgramPerticipation(Long partnershipId) {
    Partnership partnership = this.partnershipRepository.findOne(partnershipId);
    partnership.setPartnershipStatus(PartnershipStatus.Rejected);
    this.partnershipRepository.save(partnership);
  }

  @Transactional(readOnly = true)
  public PartnerProgramDefinitionDto getPartnerProgramById(Long id) {
    PartnerProgramDefinitionDto retProgram = null;
    PartnerProgramDefinition partnerProgramDef = this.partnerProgramRepository
        .findOne(id);
    partnerProgramEnforcer.get(partnerProgramDef).checkAllowed();
    retProgram = this.partnerProgramMarshaller
        .marshallPartnerProgramDefinition(partnerProgramDef);
    return retProgram;
  }

  @Transactional(readOnly = true)
  public List<PartnerProgramDefinitionDto> getPartnerProgramsForOrg(Long orgId) {
    List<PartnerProgramDefinitionDto> partnerProgramDtoList = new ArrayList<PartnerProgramDefinitionDto>();
    SocialOrganization socialOrg = this.organizationRepository.findOne(orgId);
    partnerProgramEnforcer.getForOwner(socialOrg).checkAllowed();
    List<PartnerProgramDefinition> partnerProgList = this.partnerProgramRepository
        .getProgramsForOwner(socialOrg);

    if (partnerProgList != null) {
      partnerProgramDtoList = this.partnerProgramMarshaller
          .marshallPartnerProgramDefinitions(partnerProgList);
    }

    return partnerProgramDtoList;
  }

  protected PartnerProgramDefinition createPartnerProgram(
      PartnerProgramDefinitionDto partnerProgram) {
    PartnerProgramDefinition prtnerProgramDef = null;
    SocialOrganization org = this.organizationRepository.findOne(partnerProgram
        .getOwner().getId());
    SocialPerson creator = this.memberRepository.findOne(partnerProgram
        .getCreator().getId());

    if (partnerProgram.getId() != Long.MIN_VALUE) {
      throw new IllegalArgumentException(
          "Cannot create Partner Program since it exists");
    }

    if (StringUtils.isEmpty(partnerProgram.getName())) {
      throw new IllegalArgumentException(
          "Partner Program Name cannot be NULL or Empty");
    }

    if (this.partnerProgramRepository.findByName(partnerProgram.getName(), org) != null) {
      throw new IllegalArgumentException(
          "Partner program with duplicate name cannot be created");
    }

    prtnerProgramDef = this.partnerProgramRepository.create();
    prtnerProgramDef.setName(partnerProgram.getName());
    prtnerProgramDef.setCreator(creator);
    prtnerProgramDef.setProgramOwner(org);
    prtnerProgramDef.setCreationDate(new Date());
    this.partnerProgramRepository.save(prtnerProgramDef);

    /* create the associate workspace */
    SocialActorKey actorKey = socialActorService
        .transitional_getKey(partnerProgram.getOwner().getId());

    CollaborationWorkspaceKey workspaceKey = new CollaborationWorkspaceKey(
        actorKey, partnerProgram.getName() + "_Workspace");

    NewCollaborationWorkspaceDto newCollaborationWorkspaceDto = new NewCollaborationWorkspaceDto(
        "Partner program workspace " + partnerProgram.getName());
    newCollaborationWorkspaceDto.setPartnerWorkspace(Boolean.TRUE);

    CollaborationWorkspaceDto workspaceDto = this.collaborationWorkspaceService
        .create(workspaceKey, newCollaborationWorkspaceDto);

    CollaborationWorkspace workspace = this.collaborationWorkspaceRepository
        .findOne(workspaceDto.getKey().getOwner().getName(), workspaceDto
            .getKey().getName());
    prtnerProgramDef.setPartnerProgramWorkspace(workspace);
    this.partnerProgramRepository.save(prtnerProgramDef);

    return prtnerProgramDef;
  }

  @Transactional
  public Long savePartnerProgram(PartnerProgramDefinitionDto partnerProgram) {

    log.info("Saving partner program " + partnerProgram.getName());
    Long retId = Long.MIN_VALUE;

    SocialOrganization socialOrg = this.organizationRepository
        .findOne(partnerProgram.getOwner().getId());
    PartnerProgramDefinition prtnerProgramDef = null;

    if (partnerProgram.getId() == Long.MIN_VALUE) {

      partnerProgramEnforcer.create(socialOrg).checkAllowed();
      prtnerProgramDef = this.createPartnerProgram(partnerProgram);

    } else {

      prtnerProgramDef = this.partnerProgramRepository.findOne(partnerProgram
          .getId());
      partnerProgramEnforcer.update(prtnerProgramDef).checkAllowed();
    }

    prtnerProgramDef.setDescription(partnerProgram.getDescription());
    prtnerProgramDef.setProgramDetails(partnerProgram.getProgramDetails());
    prtnerProgramDef.setLogoLocation(partnerProgram.getLogoLocation());
    prtnerProgramDef.setOutStandingInvites(partnerProgram
        .getOutStandingInvites());
    prtnerProgramDef.setTotalActiveMembers(partnerProgram
        .getTotalActiveMembers());
    prtnerProgramDef.setRequiredBusinessMemberInfoOption(partnerProgram
        .isRequiredBusinessMemberInfoOption());

    List<PartnerPreQualificationQuestion> questions = new ArrayList<PartnerPreQualificationQuestion>();
    prtnerProgramDef.setPartnerPreQualificationQuestions(questions);

    /* pre-qual questions */
    List<PartnerPreQualificationQuestionDto> preQualQuestionList = partnerProgram
        .getPreQualificationQuestions();
    log.info("Saving " + preQualQuestionList.size());

    for (PartnerPreQualificationQuestionDto preQual : preQualQuestionList) {

      PartnerPreQualificationQuestion preQualQuestion = this.partnerPreQualificationQuestionRepository
          .create();
      preQualQuestion.setQuestionIndex(preQual.getQuestionIndex());
      preQualQuestion.setQuestionText(preQual.getQuestionText());
      preQualQuestion.setAnswerType(preQual.getAnswerType());
      partnerPreQualificationQuestionRepository.save(preQualQuestion);
      this.partnerPreQualificationQuestionResponseRepository.create();
      log.info("Saving " + preQual.getQuestionText());

      for (PartnerPreQualificationAnswerChoiceDto questionChoiceDto : preQual
          .getPreQualAnswerChoice()) {
        PartnerPreQualificationAnswerChoice answerChoice = this.partnerPreQualificationAnswerChoiceRepository
            .create();
        answerChoice.setAnswerChoiceText(questionChoiceDto
            .getAnswerChoiceText());
        answerChoice.setAnswerIndex(questionChoiceDto.getAnswerIndex());
        this.partnerPreQualificationAnswerChoiceRepository.save(answerChoice);

        preQualQuestion.getAnswerChoice().add(answerChoice);
      }
      questions.add(preQualQuestion);

    }

    partnerProgram.getDeletedPreQualQuestions().clear();

    /* partner attribute list */
    List<PartnerRequiredAttributeDto> attributeList = partnerProgram
        .getPartnerRequiredInfoAttributes().getTarget();
    List<PartnerProgramRequiredAttribute> partnerDomainObjAttrList = new ArrayList<PartnerProgramRequiredAttribute>();

    for (PartnerRequiredAttributeDto attr : attributeList) {

      PartnerProgramRequiredAttribute partnerPrgAttr = this.partnerProgramRequiredAttributeRepository
          .create();
      partnerPrgAttr.setRequired(Boolean.TRUE);
      partnerPrgAttr.setAttributeType(PartnerProgramRequiredAttributeType
          .valueOf(attr.getName()));
      this.partnerProgramRequiredAttributeRepository.save(partnerPrgAttr);
      partnerDomainObjAttrList.add(partnerPrgAttr);
      log.info("Created partner attribute " + partnerPrgAttr.getId());
    }

    prtnerProgramDef.setPartnerProgramAttributes(partnerDomainObjAttrList);

    /* Benefits */
    List<PartnerProgramBenifitDto> benifits = partnerProgram
        .getPartnerProgramBenifits();
    for (PartnerProgramBenifitDto benifitDto : benifits) {
      if (benifitDto.getId().equals(Long.MIN_VALUE)) {
        PartnerProgramBenifit benifit = this.partnerProgramBenifitRepository
            .create(benifitDto.getBenifitName(), benifitDto.getBenifitDetails());
        this.partnerProgramBenifitRepository.save(benifit);
        prtnerProgramDef.getProgramBenifit().add(benifit);
      }
    }

    /* relationship category */
    List<PartnerRelationshipCategoryDto> categories = partnerProgram
        .getPartnerRelationshipCategory();
    for (PartnerRelationshipCategoryDto category : categories) {
      if (category.getId().equals(Long.MIN_VALUE)) {
        PartnerRelationshipCategory categoryObj = this.partnerRelationshipCategoryRepository
            .create(category.getPartnerRelationshipCategoryType(),
                category.getRelationshipDetails());
        categoryObj.setCustomName(category.getCustomName());
        this.partnerRelationshipCategoryRepository.save(categoryObj);
        prtnerProgramDef.getRelationshipCategory().add(categoryObj);
      }
    }

    /* update the program */
    this.partnerProgramRepository.save(prtnerProgramDef);
    partnerProgram.setId(prtnerProgramDef.getId());
    retId = prtnerProgramDef.getId();

    return retId;

  }

  @Override
  @Transactional
  public List<PartnerProgramInviteDto> getPartnerInvitesByPartnerProgramBusiness(
      Long organizationId) {

    SocialOrganization organization = this.organizationRepository
        .findOne(organizationId);

    List<PartnerProgramInviteDto> retInviteInfo = new ArrayList<PartnerProgramInviteDto>();

    List<WorkflowTransientState> transStateList = this.transientStateRepository
        .findByType(WorkflowTransientStateType.ExistingPartnerInvite);

    for (WorkflowTransientState transState : transStateList) {

      WorkflowTransientStateAttribute partnerProgAttr = transState
          .getAttributeByName(ProcessContextParameterConstants.PARTNER_PROGRAM_ID);

      WorkflowTransientStateAttribute partnerProgramBusinessAttr = transState
          .getAttributeByName(ProcessContextParameterConstants.ORGANIZATION_ID);

      if (partnerProgramBusinessAttr != null) {

        if (organization.getName().equalsIgnoreCase(
            partnerProgramBusinessAttr.getAttributeValue())) {

          WorkflowTransientStateAttribute inviteeBusinessAttr = transState
              .getAttributeByName(ProcessContextParameterConstants.ORG_INVITEE_NAME);

          WorkflowTransientStateAttribute inviteeEmailAttr = transState
              .getAttributeByName(ProcessContextParameterConstants.INVITEE_EMAIL);

          WorkflowTransientStateAttribute inviteeFirstNameAttr = transState
              .getAttributeByName(ProcessContextParameterConstants.INVITEE_FIRSTNAME);

          WorkflowTransientStateAttribute inviteeLastNameAttr = transState
              .getAttributeByName(ProcessContextParameterConstants.INVITEE_LASTNAME);

          // get the partner program
          Long programId = Long.valueOf(partnerProgAttr.getAttributeValue());
          PartnerProgramDefinitionDto program = this
              .getPartnerProgramById(programId);

          PartnerProgramInviteDto programInvite = new PartnerProgramInviteDto();
          programInvite.setProgramName(program.getName());

          if (inviteeBusinessAttr != null) {
            programInvite.setInviteeBusiness(inviteeBusinessAttr
                .getAttributeValue());
          }

          if (inviteeEmailAttr != null) {
            programInvite.setInviteeEmail(inviteeEmailAttr.getAttributeValue());
          }

          if (inviteeFirstNameAttr != null) {
            programInvite.setInviteeFirstName(inviteeFirstNameAttr
                .getAttributeValue());
          }

          if (inviteeLastNameAttr != null) {
            programInvite.setInviteeLastName(inviteeLastNameAttr
                .getAttributeValue());
          }

          programInvite.setTimeStamp(transState.getTimeStamp());
          programInvite.setWorkflowInstanceId(transState.getId());

          retInviteInfo.add(programInvite);

        }
      }
    }

    return retInviteInfo;

  }

  @Override
  @Transactional
  public List<PartnerProgramInviteDto> getPartnerInvitesByInviter(
      Long inviterParamId) {

    List<PartnerProgramInviteDto> retInviteInfo = new ArrayList<PartnerProgramInviteDto>();

    List<WorkflowTransientState> transStateList = this.transientStateRepository
        .findByType(WorkflowTransientStateType.PartnerProgramInvite);

    for (WorkflowTransientState transState : transStateList) {

      WorkflowTransientStateAttribute partnerProgAttr = transState
          .getAttributeByName(ProcessContextParameterConstants.PARTNER_PROGRAM_ID);

      WorkflowTransientStateAttribute inviterAttr = transState
          .getAttributeByName(ProcessContextParameterConstants.INVITER_ID);

      if (inviterAttr != null) {
        Long inviterId = Long.valueOf(inviterAttr.getAttributeValue());
        if (inviterParamId.equals(inviterId)) {

          WorkflowTransientStateAttribute inviteeBusinessAttr = transState
              .getAttributeByName(ProcessContextParameterConstants.ORG_INVITEE_NAME);

          WorkflowTransientStateAttribute inviteeEmailAttr = transState
              .getAttributeByName(ProcessContextParameterConstants.INVITEE_EMAIL);

          WorkflowTransientStateAttribute inviteeFirstNameAttr = transState
              .getAttributeByName(ProcessContextParameterConstants.INVITEE_FIRSTNAME);

          WorkflowTransientStateAttribute inviteeLastNameAttr = transState
              .getAttributeByName(ProcessContextParameterConstants.INVITEE_LASTNAME);

          // get the partner program
          Long programId = Long.valueOf(partnerProgAttr.getAttributeValue());
          PartnerProgramDefinitionDto program = this
              .getPartnerProgramById(programId);

          PartnerProgramInviteDto programInvite = new PartnerProgramInviteDto();
          programInvite.setProgramName(program.getName());

          if (inviteeBusinessAttr != null) {
            programInvite.setInviteeBusiness(inviteeBusinessAttr
                .getAttributeValue());
          }

          if (inviteeEmailAttr != null) {
            programInvite.setInviteeEmail(inviteeEmailAttr.getAttributeValue());
          }

          if (inviteeFirstNameAttr != null) {
            programInvite.setInviteeFirstName(inviteeFirstNameAttr
                .getAttributeValue());
          }

          if (inviteeLastNameAttr != null) {
            programInvite.setInviteeLastName(inviteeLastNameAttr
                .getAttributeValue());
          }

          programInvite.setTimeStamp(transState.getTimeStamp());
          programInvite.setWorkflowInstanceId(transState.getId());

          retInviteInfo.add(programInvite);

        }
      }
    }

    return retInviteInfo;
  }

  @Transactional
  public void initiatePartnerInvite(OrganizationBasicInfoDto participatingOrg,
      MemberInfoDto memberInfo, PartnerProgramDefinitionDto partnerProgram) {

    log.info("Initiating partner program invite flow for program "
        + partnerProgram.getId());

    // check the input params
    if (participatingOrg == null || partnerProgram == null
        || memberInfo == null) {

      StringBuilder invalidParams = new StringBuilder();
      if (participatingOrg == null) {
        invalidParams.append(" invalid partner org, ");
      }

      if (partnerProgram == null) {
        invalidParams.append(" invalid partner program selected ");
      }

      throw new PartnerManagementServiceException(
          "Invalid parameter while initiating partner invite "
              + invalidParams.toString());
    }

    if (StringUtils.isEmpty(participatingOrg.getEmail()) == true) {
      throw new CoreServiceException(
          "Cannot invite a partner since business has invalid email address");
    }

    // check whether this member is associated with any org. If no then
    // we cannot go ahead
    SocialPerson inviterMember = this.memberRepository.findOne(memberInfo
        .getId());
    if (inviterMember != null) {
      SocialEmployee socialEmpolyee = inviterMember.getEmployer();
      if (socialEmpolyee == null) {
        throw new CoreServiceException(
            "Cannot invite a partner since member is not "
                + "  associated with a business");
      }

      if (StringUtils.isEmpty(((SocialOrganization) socialEmpolyee
          .getContactFrom()).getPrimaryEmail()) == true) {
        throw new CoreServiceException(
            "Cannot invite a business partner with null email ");
      }

      if (((SocialOrganization) socialEmpolyee.getContactFrom())
          .getPrimaryEmail().equalsIgnoreCase(participatingOrg.getEmail())) {
        throw new CoreServiceException(
            "Cannot invite the same business entity as partner");
      }
    } else {
      throw new CoreServiceException(
          "Cannot invite a partner since member is NULL ");
    }

    // check whether it is already a partner if so throw an exception
    // and come out
    if (this.isPartnerForProgram(participatingOrg.getId(),
        partnerProgram.getId()) == true) {
      throw new PartnerManagementServiceException("The business entity "
          + participatingOrg.getName()
          + " is already a partner for the selected program "
          + partnerProgram.getName());
    }

    // check if there is a pending work flow for this partner program for
    // this business
    List<WorkflowTransientState> transStateList = this.transientStateRepository
        .findByType(WorkflowTransientStateType.PartnerProgramInvite);
    if (transStateList != null) {
      for (WorkflowTransientState transState : transStateList) {
        WorkflowTransientStateAttribute partnerProgAttr = transState
            .getAttributeByName(ProcessContextParameterConstants.PARTNER_PROGRAM_ID);
        if (partnerProgAttr != null) {
          if (partnerProgram.getId() == Long.valueOf(partnerProgAttr
              .getAttributeValue())) {
            WorkflowTransientStateAttribute partnerOrgAttr = transState
                .getAttributeByName(ProcessContextParameterConstants.ORGANIZATION_ID);
            if (partnerOrgAttr != null) {
              if (Long.valueOf(partnerOrgAttr.getAttributeValue()) == participatingOrg
                  .getId()) {
                throw new PartnerManagementServiceException(
                    "The business entity "
                        + participatingOrg.getName()
                        + " has been already invited this partner for the selected program "
                        + partnerProgram.getName());
              }
            }
          }
        }
      }
    }

    // now initiate the flow
    BusinessProcessDefinition businessCreationProcess = this.businessProcessDefinitionRepository
        .findByName("partnerProgramInvite");
    log.info("Partner program invite for " + businessCreationProcess.getName());

    WorkflowInitialRequest initialRequest = new WorkflowInitialRequest();
    initialRequest.setFlowDefinitionName(businessCreationProcess.getName());

    /* set the parameters */
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(ProcessContextParameterConstants.PARTNER_PROGRAM_ID,
        partnerProgram.getId());
    parameters.put(ProcessContextParameterConstants.ORGANIZATION_ID,
        participatingOrg.getId());
    parameters.put(ProcessContextParameterConstants.INVITER_ID,
        memberInfo.getId());

    /* set the parameters */
    initialRequest.setExecutionParameters(parameters);

    /* execute the work flow */
    this.workflowManager.initiate(initialRequest);

  }

  @Transactional
  public void submitPartnerProgramApprovalFormInput(
      PartnerProgramDefinitionDto partnerProg, Long partnerOrgId,
      Long memStateId, Long inviterId, String processInstanceId) {

    /* set the parameters */
    Partnership partnership = this.createPartnershipFromPartnerProgram(
        partnerOrgId, partnerProg, null);
    partnerProgramEnforcer.submitInput(partnership).checkAllowed();
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(ProcessContextParameterConstants.MEMBER_ID, inviterId);
    parameters.put(ProcessContextParameterConstants.PARTNER_PROGRAM_ID,
        partnerProg.getId());
    parameters.put(ProcessContextParameterConstants.MEMBER_TRANS_STATE_ID,
        memStateId);
    parameters.put(ProcessContextParameterConstants.PARTNER_PARTNERSHIP_ID,
        partnership.getId());
    log.info("Partner program form submission for program "
        + partnerProg.getId());

    WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
    resumeRequest.setExecutionParameters(parameters);
    resumeRequest.setWorkflowInstanceId(processInstanceId);
    this.workflowManager.resume(resumeRequest);

  }

  @Transactional(readOnly = true)
  public Boolean isPartnerForProgram(Long orgId, Long progId) {
    SocialOrganization organization = organizationRepository.findOne(orgId);
    PartnerProgramDefinition program = partnerProgramRepository.findOne(progId);
    partnerProgramEnforcer.isPartnerForProgram(organization, program)
        .checkAllowed();
    Partnership partnership = this.partnershipRepository
        .getPartnershipByProgramAndPartner(orgId, progId);
    if (partnership != null) {
      return Boolean.TRUE;
    }

    return Boolean.FALSE;
  }

  @Override
  @Transactional
  public void existingPartnerInviteComplete(
      ExistingPartnerInviteCompleteDto compleDto) {

    /* get the member and update password */
    SocialPerson businessAdmin = this.memberRepository.findOne(compleDto
        .getAdminId());
    Account memberAccount = new Account();
    memberAccount.setUserName(businessAdmin.getPrimaryEmail());
    memberAccount.setPassword(compleDto.getNewAdminPassword());
    businessAdmin.setMemberAccount(memberAccount);

    this.memberRepository.save(businessAdmin);

    /* set the parameters */

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(ProcessContextParameterConstants.MEMBER_ID,
        compleDto.getAdminId());
    parameters.put(ProcessContextParameterConstants.ORG_INVITEE_NAME,
        compleDto.getPartnerBusinessName());
    parameters.put(ProcessContextParameterConstants.PARTNER_PROGRAM_APPROVAL,
        Boolean.TRUE);
    parameters.put(ProcessContextParameterConstants.BUSINESS_EMAIL,
        compleDto.getEmailAddress());

    WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
    resumeRequest.setExecutionParameters(parameters);
    resumeRequest.setWorkflowInstanceId(compleDto.getProcessInstanceId());
    this.workflowManager.resume(resumeRequest);

  }

  @Override
  @Transactional
  public void initiateExistingPartnerInvite(
      ExistingPartnerInitiateDto initiateDto) {
    /* set the parameters */

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(ProcessContextParameterConstants.ORG_INVITEE_NAME,
        initiateDto.getInviteeBusinessName());
    parameters.put(ProcessContextParameterConstants.INVITEE_EMAIL,
        initiateDto.getInviteeEmail());
    parameters.put(ProcessContextParameterConstants.INVITEE_FIRSTNAME,
        initiateDto.getInviteeFirstName());
    parameters.put(ProcessContextParameterConstants.INVITEE_LASTNAME,
        initiateDto.getInviteeLastName());
    parameters.put(ProcessContextParameterConstants.INVITER_ID,
        initiateDto.getInviterId());
    parameters.put(ProcessContextParameterConstants.PARTNER_PROGRAM_ID,
        initiateDto.getPartnerProgramId());

    // now initiate the flow
    BusinessProcessDefinition businessCreationProcess = this.businessProcessDefinitionRepository
        .findByName("existingPartnerInvite");
    log.info("Partner program invite for " + businessCreationProcess.getName());

    WorkflowInitialRequest initialRequest = new WorkflowInitialRequest();
    initialRequest.setFlowDefinitionName(businessCreationProcess.getName());

    /* set the parameters */
    initialRequest.setExecutionParameters(parameters);

    /* execute the work flow */
    this.workflowManager.initiate(initialRequest);

  }

  @Override
  @Transactional
  public void sendPartnerInviteReminder(List<PartnerProgramInviteDto> invites,
      SocialActorKey remindSender) {

    SocialActor sender = socialActorMarshaller.unmarshallActorKey(remindSender,
        true);
    SocialOrganization partnerBusiness = null;
    String logoUrl = StringUtils.EMPTY;

    for (PartnerProgramInviteDto partnerInvite : invites) {

      WorkflowTransientState transState = this.transientStateRepository
          .findOne(partnerInvite.getWorkflowInstanceId());

      WorkflowTransientStateAttribute inviteeBusinessAttr = transState
          .getAttributeByName(ProcessContextParameterConstants.ORG_INVITEE_NAME);

      WorkflowTransientStateAttribute inviteeFirstNameAttr = transState
          .getAttributeByName(ProcessContextParameterConstants.INVITEE_FIRSTNAME);

      WorkflowTransientStateAttribute inviteeLastNameAttr = transState
          .getAttributeByName(ProcessContextParameterConstants.INVITEE_LASTNAME);

      WorkflowTransientStateAttribute partnerProgAttr = transState
          .getAttributeByName(ProcessContextParameterConstants.PARTNER_PROGRAM_ID);

      WorkflowTransientStateAttribute partnerProgramBusinessAttr = transState
          .getAttributeByName(ProcessContextParameterConstants.ORGANIZATION_ID);

      if (partnerBusiness == null) {
        partnerBusiness = (SocialOrganization) this.socialActorRepository
            .findOne(partnerProgramBusinessAttr.getAttributeValue());
      }

      Long programId = Long.valueOf(partnerProgAttr.getAttributeValue());
      PartnerProgramDefinition program = this.partnerProgramRepository
          .findOne(programId);

      List<SocialActor> recepientList = new ArrayList<SocialActor>();
      SocialActor recepient = (SocialActor) this.memberRepository
          .findOne(transState.getMemberId());
      recepientList.add(recepient);

      String hostUrl = platformCommonSettings.getPortalApplcationUrl()
          .getValue();
      StringBuilder urlBuilder = new StringBuilder();
      urlBuilder.append(hostUrl);
      urlBuilder.append("business/existingPartnerInfoEntry.xhtml?mid=")
          .append(transState.getMemberId()).append("&pid=")
          .append(transState.getProcessInstanceId());

      StringBuilder partnerLoginUrlBuilder = new StringBuilder();
      partnerLoginUrlBuilder.append(hostUrl);
      partnerLoginUrlBuilder.append("partner/partnerBrandedLogin.xhtml?oid=")
          .append(program.getProgramOwner().getName());

      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters
          .put(EmailParameterConstants.PARTNER_PROGRAM, program.getName());
      parameters.put(EmailParameterConstants.PARTNER_PROGRAM_JOIN_URL,
          urlBuilder.toString());
      parameters.put(EmailParameterConstants.INVITEE_FIRSTNAME,
          inviteeFirstNameAttr.getAttributeValue());
      parameters.put(EmailParameterConstants.INVITEE_LASTNAME,
          inviteeLastNameAttr.getAttributeValue());
      parameters.put(EmailParameterConstants.INVITER_BUSINESS,
          partnerBusiness.getBusinessName());
      parameters.put(EmailParameterConstants.INVITEE_BUSINESS,
          inviteeBusinessAttr.getAttributeValue());
      parameters.put(EmailParameterConstants.PARTNER_LOGIN_URL,
          partnerLoginUrlBuilder.toString());

      if (StringUtils.isNotBlank(partnerBusiness.getLogoUrl())) {

        logoUrl = platformCommonSettings.getPortalApplcationUrl().getValue()
            + "spring/assets" + partnerBusiness.getLogoUrl();
        log.info("Branded logo URL : " + logoUrl);
        parameters.put(ProcessContextParameterConstants.BUSINESS_LOGO, logoUrl);
      }

      NewEmail newEmail = new NewEmail(sender, recepientList,
          "partnerProgramInviteReminder.vm", parameters);
      this.emailMessagingService.send(newEmail);

    }

    // we also need to send a notification to partner admin
    Map<String, Object> parametersNotification = new HashMap<String, Object>();
    parametersNotification.put(EmailParameterConstants.PARTNER_REMINDER_LIST,
        invites);

    if (StringUtils.isNotBlank(logoUrl)) {
      parametersNotification.put(
          ProcessContextParameterConstants.BUSINESS_LOGO, logoUrl);
    }

    List<SocialActor> recepientList = new ArrayList<SocialActor>();
    Set<EmployeeRole> roles = new HashSet<EmployeeRole>();
    roles.add(EmployeeRole.PARTNER_PROGRAM_ADMIN);

    List<SocialEmployee> partnerAdmins = socialEmployeeRepository
        .findByContactFromAndRolesIn(partnerBusiness, roles);
    if (partnerAdmins != null && partnerAdmins.size() > 0) {
      for (SocialEmployee employeePartnerAdmin : partnerAdmins) {
        recepientList.add(employeePartnerAdmin.getContactTo());
      }
    }

    recepientList.add(sender);

    NewEmail newPartnerAdminReminderNotificationEmail = new NewEmail(sender,
        recepientList, "partnerAdminReminderNotification.vm",
        parametersNotification);
    this.emailMessagingService.send(newPartnerAdminReminderNotificationEmail);

    // we need to save an audit event
    NewAuditEventDto event = new NewAuditEventDto();
    List<NewAuditEventAttributeDto> attrList = new ArrayList<NewAuditEventAttributeDto>();
    event.setAttributes(attrList);

    for (PartnerProgramInviteDto partnerInvite : invites) {
      NewAuditEventAttributeDto attrEmail = new NewAuditEventAttributeDto();
      attrEmail.setAttributeType(AuditEventAttributeType.PARTNER_CONTACT_EMAIL);
      attrEmail.setValue(partnerInvite.getInviteeEmail());
      attrList.add(attrEmail);

      NewAuditEventAttributeDto attrPartnerName = new NewAuditEventAttributeDto();
      attrPartnerName.setAttributeType(AuditEventAttributeType.PARTNER_NAME);
      attrPartnerName.setValue(partnerInvite.getInviteeBusiness());
      attrList.add(attrPartnerName);

    }
    event.setEventType(AuditEventType.PARTNER_REINVITE);
    auditEvetService.create(event, remindSender, new SocialActorKey(
        partnerBusiness.getName()));
  }
}
