package com.similan.process.action.partner;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.MemberTransientStateAttributeType;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.partner.PartnerProgramDefinition;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.entity.util.WorkflowTransientStateType;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.partner.PartnerProgramDefinitionRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.constants.PageResourceConstants;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.process.action.ActionResultType;
import com.similan.service.internal.api.email.EmailParameterConstants;

@Slf4j
@Component
public class ValidatePartnerInviteInputAction implements ActivityBehaviour {
  private static final long serialVersionUID = 1L;

  @Autowired
  private SocialOrganizationRepository organizationRepository;
  @Autowired
  private WorkflowTransientStateRepository memberTransientStateRepository;
  @Autowired
  private PartnerProgramDefinitionRepository partnerProgramRepository;
  @Autowired
  private PlatformCommonSettings platformCommonSettings;

  public void execute(ActivityExecution execution) throws Exception {

    /**
     * PARTNER_PROGRAM_ID = Participating partner program ORGANIZATION_ID =
     * Participating partner org INVITER_ID = Member who invited that partner
     */
    Long partnerProgId = (Long) execution
        .getVariable(ProcessContextParameterConstants.PARTNER_PROGRAM_ID);
    Long partnerOrgId = (Long) execution
        .getVariable(ProcessContextParameterConstants.ORGANIZATION_ID);
    Long inviterId = (Long) execution
        .getVariable(ProcessContextParameterConstants.INVITER_ID);

    /* get the social organization and email */
    SocialOrganization orgTobePartner = this.organizationRepository
        .findOne(partnerOrgId);
    PartnerProgramDefinition partnerProgram = this.partnerProgramRepository
        .findOne(Long.valueOf(partnerProgId));
    String emailToSendForm = orgTobePartner.getPrimaryEmail();

    log.info("Partner program " + partnerProgId + " for org " + partnerOrgId
        + " inviter " + inviterId);

    /* create the transient state for response */
    String processInstanceId = execution.getId();
    WorkflowTransientState memberTransientState = this.memberTransientStateRepository
        .create();
    memberTransientState.setMemberId(Long.valueOf(inviterId));
    memberTransientState.setProcessInstanceId(processInstanceId);
    memberTransientState
        .setStateType(WorkflowTransientStateType.PartnerProgramInvite);

    WorkflowTransientStateAttribute partprogIdAttr = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.PARTNER_PROGRAM_ID,
            partnerProgId.toString(),
            MemberTransientStateAttributeType.STRING_TYPE);
    memberTransientState.getAttributes().add(partprogIdAttr);

    WorkflowTransientStateAttribute orgIdAttr = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.ORGANIZATION_ID,
            partnerOrgId.toString(),
            MemberTransientStateAttributeType.STRING_TYPE);
    memberTransientState.getAttributes().add(orgIdAttr);

    WorkflowTransientStateAttribute memberInviterAttr = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.INVITER_ID,
            inviterId.toString(), MemberTransientStateAttributeType.STRING_TYPE);
    memberTransientState.getAttributes().add(memberInviterAttr);
    this.memberTransientStateRepository.save(memberTransientState);

    String hostUrl = platformCommonSettings.getPortalApplcationUrl().getValue();
    StringBuilder urlFormInputBuilder = new StringBuilder();
    urlFormInputBuilder.append(hostUrl)
        .append("partner/partnerProgramApprovalInputForm.xhtml?pid=")
        .append(processInstanceId).append("&prid=").append(partnerProgId)
        .append("&oid=").append(partnerOrgId).append("&msid=")
        .append(memberTransientState.getId()).append("&mid=").append(inviterId);

    StringBuilder partnerLoginUrlBuilder = new StringBuilder();
    partnerLoginUrlBuilder.append(hostUrl);
    partnerLoginUrlBuilder
        .append(PageResourceConstants.PARTNER_BRANDED_LOGIN_PAGE)
        .append("?oid=").append(partnerProgram.getProgramOwner().getName());

    String partnerProgramFormInputUrl = urlFormInputBuilder.toString();
    execution.setVariable(
        ProcessContextParameterConstants.PARTNER_PROGRAM_FORMINPUT_URL,
        partnerProgramFormInputUrl);
    execution.setVariable(
        ProcessContextParameterConstants.PARTNER_PROGRAM_NAME,
        partnerProgram.getName());
    execution.setVariable(ProcessContextParameterConstants.BUSINESS_NAME,
        orgTobePartner.getBusinessName());
    execution.setVariable(EmailParameterConstants.PARTNER_LOGIN_URL,
        partnerLoginUrlBuilder.toString());

    execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
        emailToSendForm);
    log.info("Partner approval Url " + partnerProgramFormInputUrl
        + " email to be sent " + emailToSendForm + " partner login url "
        + partnerLoginUrlBuilder.toString());

    execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
        ActionResultType.valid.toString());

  }

}
