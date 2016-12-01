package com.similan.process.action;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.AttributeConstantUtil;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.entity.util.WorkflowTransientStateType;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.workflow.ProcessContextParameterConstants;

public class EmailChangeInputValidationAction implements ActivityBehaviour  {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SocialPersonRepository personRepository;
	@Autowired
	private PlatformCommonSettings platformCommonSettings;
	@Autowired
	private WorkflowTransientStateRepository memberTransientStateRepository;

	public void execute(ActivityExecution execution) throws Exception {
		
		/* Get the parameters */
		String newEmailAddress = (String)execution.getVariable(ProcessContextParameterConstants.MEMBER_CHANGE_NEW_EMAIL);
		Long memberId = (Long)execution.getVariable(ProcessContextParameterConstants.MEMBER_ID); 
		
		/* get the member */
		SocialPerson member = this.personRepository.findOne(memberId);
		String existingEmailAddress = member.getPrimaryEmail();
		
		/* check whether Emails are different */
		if((newEmailAddress == null) || (newEmailAddress.contentEquals(existingEmailAddress) == true)){
			execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT, ActionResultType.inValid.toString());
			return;
		}
		else {
			
			WorkflowTransientState memberTransientState = this.memberTransientStateRepository.create();
			memberTransientState.setMemberId(member.getId());
			memberTransientState.setStateType(WorkflowTransientStateType.EmailChange);
			memberTransientState.setProcessInstanceId(execution.getId());
			
			WorkflowTransientStateAttribute attribute = this.memberTransientStateRepository.createAttribute();
			attribute.setAttributeName(AttributeConstantUtil.MEMBER_NEW_EMAIL);
			attribute.setAttributeType(AttributeConstantUtil.ATTRIBUTE_TYPE_STRING);
			attribute.setAttributeValue(newEmailAddress);
			
			List<WorkflowTransientStateAttribute> attributes = new ArrayList<WorkflowTransientStateAttribute>();
			attributes.add(attribute);
			memberTransientState.setAttributes(attributes);
			this.memberTransientStateRepository.save(memberTransientState);
			
			StringBuilder urlBuilder = new StringBuilder();
	    	String hostUrl = platformCommonSettings.getPortalApplcationUrl().getValue();
	    	String processInstanceId = execution.getId();
	    	
	    	urlBuilder.append(hostUrl).append("member/memberEmailChangeComplete.xhtml?mid=")
	    	          .append(member.getId()).append("&pid=").append(processInstanceId);
	    	String memberValidationUrl = urlBuilder.toString();
	    	
	    	/* Put everything in the context */
	    	execution.setVariable(ProcessContextParameterConstants.MEMBER_VALIDATION_URL, memberValidationUrl);
	    	execution.setVariable(ProcessContextParameterConstants.TO_EMAIL, member.getPrimaryEmail());
	    	execution.setVariable(ProcessContextParameterConstants.MEMBER_CHANGE_NEW_EMAIL, newEmailAddress);
	    	execution.setVariable(ProcessContextParameterConstants.MEMBER_FIRSTNAME, member.getFirstName());
			execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT, ActionResultType.valid.toString());
		}
	}

}
