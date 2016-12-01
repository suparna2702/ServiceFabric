package com.similan.process.action.partner;

import java.util.ArrayList;
import java.util.List;

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
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.process.action.ActionResultType;

@Slf4j
@Component
public class ValidatePartnerProgramJoinInputAction implements ActivityBehaviour {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private WorkflowTransientStateRepository memberTransientStateRepository;
	@Autowired
	private PlatformCommonSettings platformCommonSettings;
	@Autowired
	private PartnerProgramDefinitionRepository partnerProgramRepository;
	@Autowired
	private SocialOrganizationRepository organizationRepository;

	/**
	 * execution
	 */
	public void execute(ActivityExecution execution) throws Exception {
		
		/**
		 * 1. Input should be org id that wants to be partner, partner program id, member id
		 * 2. Then if everything is OK create the approval URL for the email
		 * 3. Create member transient state
		 *    ORGANIZATION_ID = Organization that would be partner
		 *    MEMBER_ID = Member who has requested the partner program
		 *    PARTNER_PROGRAM_ID = Partner program for which participation is requested
		 *    PARTNER_PARTNERSHIP_ID = The Partnership information that contains answers to
		 *                             the questions that require partner program
		 */
		Long partnerOrgId = (Long)execution.getVariable(ProcessContextParameterConstants.ORGANIZATION_ID);
		Long memberAdminId = (Long)execution.getVariable(ProcessContextParameterConstants.MEMBER_ID);
		Long partnerProgramId = (Long)execution.getVariable(ProcessContextParameterConstants.PARTNER_PROGRAM_ID);
		Long partnershipId = (Long)execution.getVariable(ProcessContextParameterConstants.PARTNER_PARTNERSHIP_ID);
		
		log.info("Validating partner participation " + partnerOrgId + " org id " 
		                              + partnerProgramId + " member id " + memberAdminId);
		
		PartnerProgramDefinition partnerProgram = partnerProgramRepository.findOne(partnerProgramId);
		SocialOrganization socialOrg = partnerProgram.getProgramOwner();
		SocialOrganization partnerOrg = this.organizationRepository.findOne(partnerOrgId);
				
		String processInstanceId = execution.getId();
		String hostUrl = platformCommonSettings.getPortalApplcationUrl().getValue();
		
		/* create the transient state for response */
		WorkflowTransientState memberTransientState = this.memberTransientStateRepository.create();
		memberTransientState.setMemberId(Long.valueOf(memberAdminId));
		memberTransientState.setProcessInstanceId(processInstanceId);
		memberTransientState.setStateType(WorkflowTransientStateType.JoinPartnerProgram);
		
		List<WorkflowTransientStateAttribute> attributes = new ArrayList<WorkflowTransientStateAttribute>();
		memberTransientState.setAttributes(attributes);
		
		WorkflowTransientStateAttribute orgIdAttr = this.memberTransientStateRepository
				                                      .createAttribute(ProcessContextParameterConstants.ORGANIZATION_ID, partnerOrgId.toString(), 
				                                    		  MemberTransientStateAttributeType.STRING_TYPE);
		attributes.add(orgIdAttr);
		
		WorkflowTransientStateAttribute memberIdAttr = this.memberTransientStateRepository
				.createAttribute(ProcessContextParameterConstants.MEMBER_ID, 
				memberAdminId.toString(), MemberTransientStateAttributeType.STRING_TYPE);
		attributes.add(memberIdAttr);
		
		WorkflowTransientStateAttribute partnerProgramAttr = this.memberTransientStateRepository
				.createAttribute(ProcessContextParameterConstants.PARTNER_PROGRAM_ID,
				partnerProgramId.toString(), MemberTransientStateAttributeType.STRING_TYPE);
		attributes.add(partnerProgramAttr);
		
		WorkflowTransientStateAttribute partnershipAttr = this.memberTransientStateRepository
				.createAttribute(ProcessContextParameterConstants.PARTNER_PARTNERSHIP_ID,
				partnershipId.toString(), MemberTransientStateAttributeType.STRING_TYPE);
		attributes.add(partnershipAttr);
		
		this.memberTransientStateRepository.save(memberTransientState);
		
		StringBuilder urlApprovalBuilder = new StringBuilder();
		urlApprovalBuilder.append(hostUrl).append("partner/partnerProgramApproval.xhtml?mid=")
                  .append(memberAdminId)
                  .append("&pid=").append(processInstanceId)
                  .append("&psid=").append(partnershipId)
                  .append("&msid=").append(memberTransientState.getId());
        String partnerProgramApprovalEvalUrl = urlApprovalBuilder.toString();
        log.info("Partner approval Url " + partnerProgramApprovalEvalUrl);
        
        StringBuilder urlRejectionBuilder = new StringBuilder();
		urlRejectionBuilder.append(hostUrl).append("partner/partnerProgramApproval.xhtml?mid=")
                  .append(memberAdminId)
                  .append("&pid=").append(processInstanceId)
                  .append("&psid=").append(partnershipId)
                  .append("&msid=").append(memberTransientState.getId());
        String partnerProgramRejectionEvalUrl = urlRejectionBuilder.toString();
        
        /* Put everything in the context */
    	execution.setVariable(ProcessContextParameterConstants.PARTNER_PROGRAM_APPROVAL_URL, partnerProgramApprovalEvalUrl);
    	execution.setVariable(ProcessContextParameterConstants.PARTNER_PROGRAM_REJECTION_URL, partnerProgramRejectionEvalUrl);
    	execution.setVariable(ProcessContextParameterConstants.BUSINESS_NAME, partnerOrg.getBusinessName());
    	execution.setVariable(ProcessContextParameterConstants.PARTNER_PROGRAM_NAME, partnerProgram.getName());
    	execution.setVariable(ProcessContextParameterConstants.TO_EMAIL, socialOrg.getPrimaryEmail());
        
        
		execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT, 
	              ActionResultType.valid.toString());
	}
}
