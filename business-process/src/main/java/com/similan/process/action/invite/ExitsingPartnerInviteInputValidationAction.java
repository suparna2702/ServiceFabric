package com.similan.process.action.invite;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.MemberState;
import com.similan.domain.entity.community.MemberTransientStateAttributeType;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.partner.PartnerProgramDefinition;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.entity.util.WorkflowTransientStateType;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.partner.PartnerProgramDefinitionRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.constants.PageResourceConstants;
import com.similan.framework.manager.management.ConfigurationManagerImpl;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.process.action.ActionResultType;
import com.similan.service.internal.api.email.EmailParameterConstants;

@Slf4j
@Component
public class ExitsingPartnerInviteInputValidationAction implements
    ActivityBehaviour {
  private static final long serialVersionUID = 1L;

  @Autowired
  private SocialOrganizationRepository organizationRepository;
  @Autowired
  private SocialPersonRepository personRepository;
  @Autowired
  private ConfigurationManagerImpl configurationManager;
  @Autowired
  private WorkflowTransientStateRepository memberTransientStateRepository;
  @Autowired
  private PlatformCommonSettings platformCommonSettings;
  @Autowired
  private PartnerProgramDefinitionRepository partnerProgramDefinitionRepository;

  @Override
  public void execute(ActivityExecution execution) throws Exception {

    // we assume that the basic validation is done at UI or service layer
    String inviteeBusinessName = (String) execution
        .getVariable(ProcessContextParameterConstants.ORG_INVITEE_NAME);
    String inviteeEmail = (String) execution
        .getVariable(ProcessContextParameterConstants.INVITEE_EMAIL);
    String inviteeFirstName = (String) execution
        .getVariable(ProcessContextParameterConstants.INVITEE_FIRSTNAME);
    String inviteeLastName = (String) execution
        .getVariable(ProcessContextParameterConstants.INVITEE_LASTNAME);
    Long inviterId = (Long) execution
        .getVariable(ProcessContextParameterConstants.INVITER_ID);
    Long partnerProgramId = (Long) execution
        .getVariable(ProcessContextParameterConstants.PARTNER_PROGRAM_ID);

    log.info("Member Inviting " + inviterId + " business to be invited "
        + inviteeBusinessName + " invitee business email " + inviteeEmail
        + " in partner program " + partnerProgramId + " invitee first name "
        + inviteeFirstName + " invitee last name " + inviteeLastName);

    if (StringUtils.isBlank(inviteeBusinessName)
        || StringUtils.isBlank(inviteeEmail)
        || StringUtils.isBlank(inviteeFirstName)
        || StringUtils.isBlank(inviteeLastName) || inviterId == null
        || partnerProgramId == null) {
      log.info("invalid input ");
      execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
          ActionResultType.inValid.toString());
      return;
    }

    SocialOrganization existingBusiness = this.organizationRepository
        .findByBusinessNameAndPrimaryEmail(inviteeBusinessName, inviteeEmail);

    PartnerProgramDefinition partnerProgram = this.partnerProgramDefinitionRepository
        .findOne(partnerProgramId);

    /* save the validation */
    WorkflowTransientState partnerTransientState = this.memberTransientStateRepository
        .create();
    this.memberTransientStateRepository.save(partnerTransientState);

    partnerTransientState.setProcessInstanceId(execution.getId());
    partnerTransientState
        .setStateType(WorkflowTransientStateType.ExistingPartnerInvite);

    List<WorkflowTransientStateAttribute> attributes = new ArrayList<WorkflowTransientStateAttribute>();
    partnerTransientState.setAttributes(attributes);

    WorkflowTransientStateAttribute stateAttr = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.ORG_INVITEE_NAME,
            inviteeBusinessName, MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(stateAttr);

    stateAttr = this.memberTransientStateRepository.createAttribute(
        ProcessContextParameterConstants.INVITEE_EMAIL, inviteeEmail,
        MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(stateAttr);

    stateAttr = this.memberTransientStateRepository.createAttribute(
        ProcessContextParameterConstants.INVITEE_FIRSTNAME, inviteeFirstName,
        MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(stateAttr);

    stateAttr = this.memberTransientStateRepository.createAttribute(
        ProcessContextParameterConstants.INVITEE_LASTNAME, inviteeLastName,
        MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(stateAttr);

    stateAttr = this.memberTransientStateRepository.createAttribute(
        ProcessContextParameterConstants.INVITER_ID, inviterId.toString(),
        MemberTransientStateAttributeType.LONG_TYPE);
    attributes.add(stateAttr);

    stateAttr = this.memberTransientStateRepository.createAttribute(
        ProcessContextParameterConstants.PARTNER_PROGRAM_ID, partnerProgram
            .getId().toString(), MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(stateAttr);

    stateAttr = this.memberTransientStateRepository.createAttribute(
        ProcessContextParameterConstants.ORGANIZATION_ID, partnerProgram
            .getProgramOwner().getName(),
        MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(stateAttr);

    stateAttr = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.PARTNER_PROGRAM_NAME,
            partnerProgram.getName(),
            MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(stateAttr);

    stateAttr = this.memberTransientStateRepository.createAttribute(
        ProcessContextParameterConstants.PARTNER_PROGRAM_DESCR,
        partnerProgram.getDescription(),
        MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(stateAttr);

    // clickable URL for email
    StringBuilder urlBuilder = new StringBuilder();
    String hostUrl = platformCommonSettings.getPortalApplcationUrl().getValue();
    urlBuilder.append(hostUrl);

    if (existingBusiness != null) {
      // case of existing business that exists in BR. We are not currently
      // dealing with this case
      log.info("invalid since existing business case is not supported ");
      execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
          ActionResultType.inValid.toString());

    } else {
      // new business partner that does not exist in BR

      String name = UUID.randomUUID().toString();
      SocialPerson businessAdmin = this.personRepository.create(name);
      businessAdmin.setFirstName(inviteeFirstName);
      businessAdmin.setLastName(inviteeLastName);
      businessAdmin.setPrimaryEmail(inviteeEmail);
      businessAdmin.setState(MemberState.Pending);
      this.personRepository.save(businessAdmin);

      partnerTransientState.setMemberId(businessAdmin.getId());

      urlBuilder.append("business/existingPartnerInfoEntry.xhtml?mid=")
          .append(businessAdmin.getId()).append("&pid=")
          .append(execution.getId());

      StringBuilder partnerLoginUrlBuilder = new StringBuilder();
      partnerLoginUrlBuilder.append(hostUrl);
      partnerLoginUrlBuilder
          .append(PageResourceConstants.PARTNER_BRANDED_LOGIN_PAGE)
          .append("?oid=").append(partnerProgram.getProgramOwner().getName());

      execution.setVariable(
          ProcessContextParameterConstants.PARTNER_PROGRAM_NAME,
          partnerProgram.getName());

      execution.setVariable(ProcessContextParameterConstants.PARTNER_INFO_URL,
          urlBuilder.toString());

      execution.setVariable(ProcessContextParameterConstants.PARTNER_INFO_URL,
          urlBuilder.toString());

      execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
          ActionResultType.newPartner.toString());

      execution.setVariable(ProcessContextParameterConstants.BUSINESS_NAME,
          partnerProgram.getProgramOwner().getDisplayName());

      execution.setVariable(EmailParameterConstants.PARTNER_LOGIN_URL,
          partnerLoginUrlBuilder.toString());

      if (!StringUtils.isBlank(partnerProgram.getProgramOwner().getImage())) {
        String logoUrl = platformCommonSettings.getPortalApplcationUrl()
            .getValue()
            + "spring/assets"
            + partnerProgram.getProgramOwner().getImage();
        log.info("Branded logo URL : " + logoUrl);
        execution.setVariable(ProcessContextParameterConstants.BUSINESS_LOGO,
            logoUrl);
      }

    }

    this.memberTransientStateRepository.save(partnerTransientState);

    execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
        inviteeEmail);

  }

}
