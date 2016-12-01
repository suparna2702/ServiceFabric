package com.similan.process.action.partner;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.partner.Partnership;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.partner.PartnerProgramDefinitionRepository;
import com.similan.domain.repository.partner.PartnershipRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.process.action.ActionResultType;
import com.similan.service.api.PartnerManagementService;

@Slf4j
@Component
public class PartnerProgramApprovalResponseEvaluationAction implements
    ActivityBehaviour {
  private static final long serialVersionUID = 1L;

  @Autowired
  private SocialOrganizationRepository organizationRepository;
  @Autowired
  private WorkflowTransientStateRepository memberTransientStateRepository;
  @Autowired
  private PartnershipRepository partnershipRepository;
  @Autowired
  private PartnerProgramDefinitionRepository partnerProgramRepository;
  @Autowired
  private PartnerManagementService partnerManagementService;

  public void execute(ActivityExecution execution) throws Exception {

    Long memberStateId = (Long) execution
        .getVariable(ProcessContextParameterConstants.MEMBER_TRANS_STATE_ID);
    Boolean approved = (Boolean) execution
        .getVariable(ProcessContextParameterConstants.PARTNER_PROGRAM_APPROVAL);
    String comment = (String) execution
        .getVariable(ProcessContextParameterConstants.PARTNERSHIP_COMMENT);
    log.info("Partner approval " + approved + " member state id "
        + memberStateId);

    WorkflowTransientState tranState = memberTransientStateRepository
        .findByProcessInstance(execution.getId());
    String partnerOrgId = tranState.getAttributeByName(
        ProcessContextParameterConstants.ORGANIZATION_ID).getAttributeValue();
    String partnerProgramId = tranState.getAttributeByName(
        ProcessContextParameterConstants.PARTNER_PROGRAM_ID)
        .getAttributeValue();
    String partnershipId = tranState.getAttributeByName(
        ProcessContextParameterConstants.PARTNER_PARTNERSHIP_ID)
        .getAttributeValue();

    SocialOrganization partnerSocialOrg = this.organizationRepository
        .findOne(Long.valueOf(partnerOrgId));
    Partnership partnership = this.partnershipRepository.findOne(Long
        .valueOf(partnershipId));

    partnership.setComment(comment);
    this.partnershipRepository.save(partnership);

    if (approved.booleanValue() == true) {

      this.partnerManagementService.approvePartnerProgramPerticipation(
          Long.valueOf(partnerProgramId), Long.valueOf(partnershipId),
          Long.valueOf(partnerOrgId));
      execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
          partnerSocialOrg.getPrimaryEmail());
      execution.setVariable(
          ProcessContextParameterConstants.PARTNERSHIP_COMMENT,
          partnership.getComment());
      execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
          ActionResultType.valid.toString());
      log.info("Approved partner program " + partnershipId);

    } else {

      this.partnerManagementService.rejectPartnerProgramPerticipation(Long
          .valueOf(partnershipId));
      execution.setVariable(
          ProcessContextParameterConstants.PARTNERSHIP_COMMENT,
          partnership.getComment());
      execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
          ActionResultType.inValid.toString());
      execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
          partnerSocialOrg.getPrimaryEmail());
      log.info("Rejected partner program " + partnershipId);
    }

    this.memberTransientStateRepository.delete(tranState);

  }

}
