package com.similan.process.action.invite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.acccount.BusinessAccountType;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceParticipation;
import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.MemberState;
import com.similan.domain.entity.community.OrganizationStatus;
import com.similan.domain.entity.community.OrganizationType;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialMemberVisibilityType;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.partner.PartnerProgramDefinition;
import com.similan.domain.entity.partner.Partnership;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.repository.account.BusinessAccountTypeRepository;
import com.similan.domain.repository.community.SocialEmployeeRepository;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.partner.PartnerProgramDefinitionRepository;
import com.similan.domain.repository.partner.PartnershipRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.process.action.ActionResultType;
import com.similan.service.api.PartnerManagementService;
import com.similan.service.api.community.dto.basic.BusinessAccountSubscriptionType;
import com.similan.service.api.community.dto.basic.SocialEmployeeType;
import com.similan.service.internal.api.email.EmailInternalService;
import com.similan.service.internal.api.email.io.NewEmail;
import com.similan.service.internal.api.event.EventInternalService;
import com.similan.service.internal.api.event.io.partner.PartnerProgramJoinEvent;

@Slf4j
@Component
public class PartnerInviteInfoValidationAction implements ActivityBehaviour {
  private static final long serialVersionUID = 1L;

  @Autowired
  private SocialPersonRepository memberRepository;
  @Autowired
  private WorkflowTransientStateRepository memberTransientStateRepository;
  @Autowired
  private PlatformCommonSettings platformCommonSettings;
  @Autowired
  private SocialOrganizationRepository organizationRepository;
  @Autowired
  private SocialEmployeeRepository socialEmployeeRepository;
  @Autowired
  private PartnershipRepository partnershipRepository;
  @Autowired
  private PartnerProgramDefinitionRepository partnerProgramDefinitionRepository;
  @Autowired
  private PartnerManagementService partnerManagementService;
  @Autowired
  private BusinessAccountTypeRepository businessAccountTypeRepository;
  @Autowired
  private EventInternalService eventInternalService;
  @Autowired
  private EmailInternalService emailMessagingService;

  @Override
  public void execute(ActivityExecution execution) throws Exception {
    WorkflowTransientState partnerInviteState = memberTransientStateRepository
        .findByProcessInstance(execution.getId());

    Long adminId = partnerInviteState.getMemberId();
    SocialPerson businessAdmin = this.memberRepository.findOne(adminId);
    businessAdmin.setState(MemberState.Active);
    businessAdmin.setVisibilityType(SocialMemberVisibilityType.VisiblePublic);

    String inviteeBusinessName = (String) execution
        .getVariable(ProcessContextParameterConstants.ORG_INVITEE_NAME);

    String inviteeOrganizationEmail = (String) execution
        .getVariable(ProcessContextParameterConstants.BUSINESS_EMAIL);

    Boolean approved = (Boolean) execution
        .getVariable(ProcessContextParameterConstants.PARTNER_PROGRAM_APPROVAL);

    log.info("Business partner name " + inviteeBusinessName
        + " business email " + inviteeOrganizationEmail + " approved "
        + approved);

    // create a business entity and get the admin
    SocialOrganization partnerBusiness = this.organizationRepository.create(
        OrganizationType.RESELLER, UUID.randomUUID().toString());
    partnerBusiness.setBusinessName(inviteeBusinessName);
    partnerBusiness.setPrimaryEmail(inviteeOrganizationEmail);
    partnerBusiness.setStatus(OrganizationStatus.Active);
    partnerBusiness.setVisibilityType(SocialMemberVisibilityType.VisiblePublic);
    BusinessAccountType accountType = businessAccountTypeRepository
        .findByName(BusinessAccountSubscriptionType.BUSINESS_STANDARD);

    partnerBusiness.setAccountType(accountType);
    partnerBusiness.setAccountType(accountType);

    this.organizationRepository.save(partnerBusiness);

    // create employee
    Set<EmployeeRole> roles = new HashSet<EmployeeRole>();
    roles.add(EmployeeRole.BUSINESS_ADMIN);
    roles.add(EmployeeRole.PARTNER_PROGRAM_ADMIN);
    roles.add(EmployeeRole.MANAGEMENT_WORKSPACE_ADMIN);

    SocialEmployee employee = socialEmployeeRepository.create(
        SocialEmployeeType.Admin, partnerBusiness, businessAdmin, roles);
    socialEmployeeRepository.save(employee);

    // also set the employee
    this.memberRepository.save(businessAdmin);

    if (approved == Boolean.TRUE) {
      execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
          ActionResultType.valid.toString());

      // create partnership now
      String partnerProgramId = partnerInviteState.getAttributeByName(
          ProcessContextParameterConstants.PARTNER_PROGRAM_ID)
          .getAttributeValue();
      PartnerProgramDefinition programDef = partnerProgramDefinitionRepository
          .findOne(Long.valueOf(partnerProgramId));

      Partnership partnership = this.partnershipRepository.create(programDef,
          partnerBusiness);
      this.partnershipRepository.save(partnership);
      log.info("Partner program " + programDef.getName());

      partnerManagementService.approvePartnerProgramPerticipation(
          programDef.getId(), partnership.getId(), partnerBusiness.getId());

      // put the variables for email
      execution.setVariable(ProcessContextParameterConstants.BUSINESS_NAME,
          programDef.getProgramOwner().getDisplayName());

      execution.setVariable(ProcessContextParameterConstants.ORGANIZATION_ID,
          programDef.getProgramOwner().getId());

      execution.setVariable(
          ProcessContextParameterConstants.PARTNER_PROGRAM_NAME,
          programDef.getName());

      execution.setVariable(ProcessContextParameterConstants.ORG_INVITEE_NAME,
          partnerBusiness.getDisplayName());

      execution.setVariable(ProcessContextParameterConstants.INVITEE_EMAIL,
          businessAdmin.getPrimaryEmail());

      execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
          programDef.getProgramOwner().getPrimaryEmail());

      // send event
      PartnerProgramJoinEvent joinEvent = new PartnerProgramJoinEvent();
      joinEvent.setPartnershipId(partnership.getId());
      joinEvent.setPartnerProgramId(programDef.getId());
      this.eventInternalService.fire(joinEvent);

      //send email only if configuration is set to send email
      if (programDef.getPartnerProgramWorkspace().getConfig().getShowActivity() != false) {

        // send the event and email
        List<CollaborationWorkspaceParticipation> participantList = programDef
            .getPartnerProgramWorkspace().getParticipations();
        List<SocialActor> participants = new ArrayList<SocialActor>();

        for (CollaborationWorkspaceParticipation partner : participantList) {
          participants.add(partner.getParticipant());
        }

        // send notification email to other partners
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("partnerProgramName", programDef.getName());
        parameters
            .put("partnerName", partnership.getPartner().getDisplayName());

        if (!StringUtils.isBlank(programDef.getProgramOwner()
            .getImage())) {

          String logoUrl = platformCommonSettings.getPortalApplcationUrl()
              .getValue()
              + "spring/assets"
              + programDef.getProgramOwner().getImage();

          log.info("Branded logo URL : " + logoUrl);

          execution.setVariable(ProcessContextParameterConstants.BUSINESS_LOGO,
              logoUrl);
        }

        NewEmail newEmail = new NewEmail(partnership.getPartner(),
            participants, "partnerProgramJoin.vm", parameters);
        this.emailMessagingService.send(newEmail);
      }

    } else {
      execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
          ActionResultType.inValid.toString());

    }

    // we dont need this anymore
    memberTransientStateRepository.delete(partnerInviteState);

  }
}
