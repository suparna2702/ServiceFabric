package com.similan.process.action.partner;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.community.MemberTransientStateAttributeType;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.partner.PartnerProgramDefinition;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.partner.PartnerProgramDefinitionRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.process.action.ActionResultType;

@Slf4j
@Component
public class ValidatePartnerProgramResponseAction implements ActivityBehaviour {
  private static final long serialVersionUID = 1L;

  @Autowired
  private PlatformCommonSettings platformCommonSettings;
  @Autowired
  private WorkflowTransientStateRepository memberTransientStateRepository;
  @Autowired
  private SocialOrganizationRepository organizationRepository;
  @Autowired
  private PartnerProgramDefinitionRepository partnerProgramRepository;

  public void execute(ActivityExecution execution) throws Exception {
    log.info("Evaluating partner form response ");
    /**
     * 1. Input should be org id that wants to be partner, partner program id,
     * member id 2. Then if everything is OK create the approval URL for the
     * email 3. Create member transient state ORGANIZATION_ID = Organization
     * that would be partner MEMBER_ID = Member who has requested the partner
     * program PARTNER_PROGRAM_ID = Partner program for which participation is
     * requested PARTNER_PARTNERSHIP_ID = The Partnership information that
     * contains answers to the questions that require partner program
     */
    Long memberStateId = (Long) execution
        .getVariable(ProcessContextParameterConstants.MEMBER_TRANS_STATE_ID);
    Long partnershipId = (Long) execution
        .getVariable(ProcessContextParameterConstants.PARTNER_PARTNERSHIP_ID);
    String processInstanceId = execution.getId();

    WorkflowTransientState tranState = memberTransientStateRepository
        .findByProcessInstance(processInstanceId);

    /* update it with partnership id */
    WorkflowTransientStateAttribute partnersipAttr = this.memberTransientStateRepository
        .createAttribute(
            ProcessContextParameterConstants.PARTNER_PARTNERSHIP_ID,
            partnershipId.toString(),
            MemberTransientStateAttributeType.STRING_TYPE);
    tranState.getAttributes().add(partnersipAttr);
    memberTransientStateRepository.save(tranState);

    String partnerProgramId = tranState.getAttributeByName(
        ProcessContextParameterConstants.PARTNER_PROGRAM_ID)
        .getAttributeValue();
    String partnerOrgId = tranState.getAttributeByName(
        ProcessContextParameterConstants.ORGANIZATION_ID).getAttributeValue();

    String hostUrl = platformCommonSettings.getPortalApplcationUrl().getValue();

    StringBuilder urlApprovalBuilder = new StringBuilder();
    urlApprovalBuilder.append(hostUrl)
        .append("partner/partnerProgramApproval.xhtml?pid=")
        .append(processInstanceId).append("&psid=").append(partnershipId)
        .append("&msid=").append(memberStateId);
    String partnerProgramApprovalEvalUrl = urlApprovalBuilder.toString();
    log.info("Partner approval Url " + partnerProgramApprovalEvalUrl);

    StringBuilder urlRejectionBuilder = new StringBuilder();
    urlRejectionBuilder.append(hostUrl)
        .append("partner/partnerProgramApproval.xhtml?pid=")
        .append(processInstanceId).append("&psid=").append(partnershipId)
        .append("&msid=").append(memberStateId);
    String partnerProgramRejectionEvalUrl = urlRejectionBuilder.toString();

    /* Put everything in the context */
    PartnerProgramDefinition partnerProgram = this.partnerProgramRepository
        .findOne(Long.valueOf(partnerProgramId));
    log.info("Partner inviter business email "
        + partnerProgram.getProgramOwner().getPrimaryEmail());

    //we will try to send the email to Partner Admin
    String toEmail = partnerProgram.getProgramOwner().getPrimaryEmail();
    CollaborationWorkspace partnerWorkspace = partnerProgram
        .getPartnerProgramWorkspace();

    if (partnerWorkspace != null) {
      SocialPerson creator = (SocialPerson) partnerWorkspace.getCreator();
      toEmail = creator.getPrimaryEmail();
    }

    SocialOrganization partnerOrg = this.organizationRepository.findOne(Long
        .valueOf(partnerOrgId));
    execution.setVariable(
        ProcessContextParameterConstants.PARTNER_PROGRAM_APPROVAL_URL,
        partnerProgramApprovalEvalUrl);
    execution.setVariable(
        ProcessContextParameterConstants.PARTNER_PROGRAM_REJECTION_URL,
        partnerProgramRejectionEvalUrl);
    execution.setVariable(ProcessContextParameterConstants.BUSINESS_NAME,
        partnerOrg.getBusinessName());
    execution.setVariable(
        ProcessContextParameterConstants.PARTNER_PROGRAM_NAME,
        partnerProgram.getName());
    execution.setVariable(ProcessContextParameterConstants.TO_EMAIL, toEmail);

    execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
        ActionResultType.valid.toString());

  }
}
