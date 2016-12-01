package com.similan.process.action;

import java.util.Date;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.Account;
import com.similan.domain.entity.community.MemberJoinIntent;
import com.similan.domain.entity.community.MemberState;
import com.similan.domain.entity.community.MemberTransientStateAttributeType;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.entity.util.WorkflowTransientStateType;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.service.api.connection.dto.basic.ContactType;

@Slf4j
@Component
public class LinkedInInvitationValidateAction implements ActivityBehaviour {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SocialPersonRepository memberRepository;
	@Autowired
	private WorkflowTransientStateRepository memberTransientStateRepository;
	@Autowired
	private PlatformCommonSettings platformCommonSettings;

	public void execute(ActivityExecution execution) throws Exception {
	    	
    	/**
    	 * 1. Get all the necessary parameters from the context
    	 * 2. Create a member
    	 * 3. Generate the Validation 
    	 */
	
    	String firstName = (String)execution.getVariable(ProcessContextParameterConstants.INVITEE_FIRSTNAME);
    	String lastName = (String)execution.getVariable(ProcessContextParameterConstants.INVITEE_LASTNAME);
    	String userName = (String)execution.getVariable(ProcessContextParameterConstants.USER_NAME);
    	String password = (String)execution.getVariable(ProcessContextParameterConstants.PASSWORD);
    	String linkedInId = (String)execution.getVariable(ProcessContextParameterConstants.INVITEE_LINKEDIN_ID);
    	Long inviterId = (Long)execution.getVariable(ProcessContextParameterConstants.INVITER_ID);
    	/* for the time being create a basic member */
    	Account memberAccount = new Account();
    	memberAccount.setUserName(userName);
    	memberAccount.setPassword(password);
    	
    	String name = UUID.randomUUID().toString();
    	SocialPerson member = memberRepository.create(name);
    	member.setMemberAccount(memberAccount);
    	member.setFirstName(firstName);
    	member.setLastName(lastName);
    	member.setState(MemberState.Pending);
    	member.setJoinMethod(MemberJoinIntent.JoinByInvite);
    	member.setStartDate(new Date());
    	
    	this.memberRepository.save(member);
    	String processInstanceId = execution.getId();
    	
    	StringBuilder urlBuilder = new StringBuilder();
    	String hostUrl = platformCommonSettings.getPortalApplcationUrl().getValue();
    	System.out.println("Host URL " + hostUrl);
    	
    	urlBuilder.append(hostUrl).append("member/memberInviteSignupInput.xhtml?mid=")
    	          .append(member.getId()).append("&pid=").append(processInstanceId);
    	String memberValidationUrl = urlBuilder.toString();
    	
    	/* Put everything in the context */
    	execution.setVariable(ProcessContextParameterConstants.MEMBER_VALIDATION_URL, memberValidationUrl);
    	
    	log.info("Member validation Url " + memberValidationUrl);
    	
    	/* save the validation */
    	WorkflowTransientState memberTransientState = this.memberTransientStateRepository.create();
    	memberTransientState.setMemberId(Long.valueOf(member.getId()));
		memberTransientState.setProcessInstanceId(processInstanceId);
		memberTransientState.setStateType(WorkflowTransientStateType.MemberInvite);
		
		WorkflowTransientStateAttribute memberInviterAttr = this.memberTransientStateRepository
				.createAttribute(ProcessContextParameterConstants.INVITER_ID, Long.toString(inviterId), 
				                 MemberTransientStateAttributeType.LONG_TYPE);
		memberTransientState.getAttributes().add(memberInviterAttr);
		
		
		WorkflowTransientStateAttribute linkedInIdAttr = this.memberTransientStateRepository
				.createAttribute(ProcessContextParameterConstants.INVITEE_LINKEDIN_ID, linkedInId, 
				                 MemberTransientStateAttributeType.STRING_TYPE);
		memberTransientState.getAttributes().add(linkedInIdAttr);

		WorkflowTransientStateAttribute inviteType  = this.memberTransientStateRepository
				.createAttribute(ProcessContextParameterConstants.MEMBER_INVITE_TYPE, ContactType.Other.toString(), 
				                 MemberTransientStateAttributeType.STRING_TYPE);
		memberTransientState.getAttributes().add(inviteType);
		
    	this.memberTransientStateRepository.save(memberTransientState);
    	
    	execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT, ActionResultType.valid.toString());
    	
	}
}
