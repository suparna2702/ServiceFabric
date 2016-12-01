package com.similan.process.action.invite;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.Account;
import com.similan.domain.entity.community.MemberState;
import com.similan.domain.entity.community.MemberTransientStateAttributeType;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.process.action.ActionResultType;

@Slf4j
@Component
public class BusinessInviteInfoValidationAction implements ActivityBehaviour {
	private static final long serialVersionUID = 1L;

	private SocialOrganizationRepository organizationRepository;
	@Autowired
	private SocialPersonRepository personRepository;
	@Autowired
	private WorkflowTransientStateRepository memberTransientStateRepository;
	@Autowired
	private PlatformCommonSettings platformCommonSettings;

	/**
	 * execute the action
	 */
	public void execute(ActivityExecution execution) throws Exception {
		
		/**
		 * 1. Check whether all the fields are correct
		 * 2. Create a business entity
		 * 3. Put the right data in the context for further email
		 */
		
		Long memberInviteeId = (Long)execution.getVariable(ProcessContextParameterConstants.MEMBER_ID);
		log.info("Member ID invited " + memberInviteeId);
		
		WorkflowTransientState memberTransientState = this.memberTransientStateRepository.findByProcessInstance(execution.getId());
		String businessName = memberTransientState.getAttributeByName(ProcessContextParameterConstants.ORG_INVITEE_NAME).getAttributeValue();
		String inviterId = memberTransientState.getAttributeByName(ProcessContextParameterConstants.INVITER_ID).getAttributeValue();
		
		log.info("Business name to be created " + businessName + " inviter id " + inviterId);
		
		String email = memberTransientState.getAttributeByName(ProcessContextParameterConstants.INVITEE_EMAIL).getAttributeValue();
		
		String businessPhone = (String)execution.getVariable(ProcessContextParameterConstants.BUSINESS_PHONE_NUMBER); 
		String businessEmail = (String)execution.getVariable(ProcessContextParameterConstants.BUSINESS_EMAIL);
		String businessAddrStreet = (String)execution.getVariable(ProcessContextParameterConstants.BUSINESS_ADDR_STREET); 
		String businessAddrCity = (String)execution.getVariable(ProcessContextParameterConstants.BUSINESS_ADDR_CITY);
		String businessAddrState = (String)execution.getVariable(ProcessContextParameterConstants.BUSINESS_ADDR_STATE); 
		String businessAddrZipCode = (String)execution.getVariable(ProcessContextParameterConstants.BUSINESS_ADDR_ZIPCODE); 
		String businessAddrCountry = (String)execution.getVariable(ProcessContextParameterConstants.BUSINESS_ADDR_COUNTRY); 
		
		/* info on member to be created */
    	String firstName = (String)execution.getVariable(ProcessContextParameterConstants.MEMBER_FIRSTNAME);
    	String lastName = (String)execution.getVariable(ProcessContextParameterConstants.MEMBER_LASTNAME);
    	String password = (String)execution.getVariable(ProcessContextParameterConstants.PASSWORD);

		if(memberInviteeId != null){
			
			/* create the admin member */
			SocialPerson memberOrgAdmin = this.personRepository.findOne(memberInviteeId);
			memberOrgAdmin.setFirstName(firstName);
			memberOrgAdmin.setLastName(lastName);
			memberOrgAdmin.setPrimaryEmail(email);
			
			Account memberOrgAdminAcc = new Account();
			memberOrgAdminAcc.setPassword(password);
			memberOrgAdmin.setMemberAccount(memberOrgAdminAcc);
			memberOrgAdmin.setState(MemberState.Active);
			memberOrgAdmin.setStartDate(new Date());
			this.personRepository.save(memberOrgAdmin);
			
			
			log.info("Updated member " + memberOrgAdmin.getId());
			
			/* member transient state */
			memberTransientState.setMemberId(memberOrgAdmin.getId());
			WorkflowTransientStateAttribute attributeBusinessName = this.memberTransientStateRepository
					.createAttribute(ProcessContextParameterConstants.BUSINESS_NAME,
					businessName, MemberTransientStateAttributeType.STRING_TYPE);
			memberTransientState.getAttributes().add(attributeBusinessName);
			
			WorkflowTransientStateAttribute attributeInviterId = this.memberTransientStateRepository
					          .createAttribute(ProcessContextParameterConstants.INVITER_ID, inviterId, 
					        		  MemberTransientStateAttributeType.STRING_TYPE);
			memberTransientState.getAttributes().add(attributeInviterId);
			
			WorkflowTransientStateAttribute attributeInviteeId = this.memberTransientStateRepository
			          .createAttribute(ProcessContextParameterConstants.INVITEE_ID, memberOrgAdmin.getId().toString(), 
			        		  MemberTransientStateAttributeType.STRING_TYPE);
	        memberTransientState.getAttributes().add(attributeInviteeId);


			WorkflowTransientStateAttribute attributeBusinessPhoneNumber = this.memberTransientStateRepository
					                 .createAttribute(ProcessContextParameterConstants.BUSINESS_PHONE_NUMBER, businessPhone, 
					                		 MemberTransientStateAttributeType.STRING_TYPE);
			memberTransientState.getAttributes().add(attributeBusinessPhoneNumber);

			WorkflowTransientStateAttribute attributeBusinessEmail = this.memberTransientStateRepository
					.createAttribute(ProcessContextParameterConstants.BUSINESS_EMAIL, businessEmail, 
							MemberTransientStateAttributeType.STRING_TYPE);
			memberTransientState.getAttributes().add(attributeBusinessEmail);

			WorkflowTransientStateAttribute attributeBusinessAddrStreet = this.memberTransientStateRepository
					.createAttribute(
							ProcessContextParameterConstants.BUSINESS_ADDR_STREET,
							businessAddrStreet,
							MemberTransientStateAttributeType.STRING_TYPE);
			memberTransientState.getAttributes().add(attributeBusinessAddrStreet);

			WorkflowTransientStateAttribute attributeBusinessAddrCity = this.memberTransientStateRepository
					.createAttribute(
							ProcessContextParameterConstants.BUSINESS_ADDR_CITY,
							businessAddrCity,
							MemberTransientStateAttributeType.STRING_TYPE);
			memberTransientState.getAttributes().add(attributeBusinessAddrCity);

			WorkflowTransientStateAttribute attributeBusinessAddrState = this.memberTransientStateRepository
					.createAttribute(
							ProcessContextParameterConstants.BUSINESS_ADDR_STATE,
							businessAddrState,
							MemberTransientStateAttributeType.STRING_TYPE);
			memberTransientState.getAttributes().add(attributeBusinessAddrState);

			WorkflowTransientStateAttribute attributeBusinessAddrZipCode = this.memberTransientStateRepository
					.createAttribute(
							ProcessContextParameterConstants.BUSINESS_ADDR_ZIPCODE,
							businessAddrZipCode,
							MemberTransientStateAttributeType.STRING_TYPE);
			memberTransientState.getAttributes().add(attributeBusinessAddrZipCode);

			WorkflowTransientStateAttribute attributeBusinessAddrCountry = this.memberTransientStateRepository
					.createAttribute(
							ProcessContextParameterConstants.BUSINESS_ADDR_COUNTRY,
							businessAddrCountry,
							MemberTransientStateAttributeType.STRING_TYPE);
			memberTransientState.getAttributes().add(attributeBusinessAddrCountry);

			memberTransientStateRepository.save(memberTransientState);
			
			/* Prepare the URL */
			StringBuilder businessApproveUrlBuilder = new StringBuilder();
	    	String hostUrl = platformCommonSettings.getPortalApplcationUrl().getValue();
	    	String processInstanceId = execution.getId();
	    	
	    	businessApproveUrlBuilder.append(hostUrl).append("business/businessEntityApproval.xhtml?mid=")
	    	          .append(memberOrgAdmin.getId()).append("&pid=").append(processInstanceId);
	    	String businessApproveUrl = businessApproveUrlBuilder.toString();
	    	
	    	StringBuilder rejectUrlBuilder = new StringBuilder();
	    	rejectUrlBuilder.append(hostUrl).append("business/businessEntityRejection.xhtml?mid=")
	                                       .append(memberOrgAdmin.getId()).append("&pid=").append(processInstanceId);
	        String businessCreationRejectUrl = rejectUrlBuilder.toString();
	        
	        /* set the context parameters properly */
			log.info("admin email " + 
			             this.platformCommonSettings.getAdminEmailSenderAddress().getValue());
			execution.setVariable(ProcessContextParameterConstants.ADMIN_EMAIL, 
					     this.platformCommonSettings.getAdminEmailSenderAddress().getValue());
			
	    	execution.setVariable(ProcessContextParameterConstants.BUSINESS_ENTITY_APPROVE_URL, businessApproveUrl);
	    	execution.setVariable(ProcessContextParameterConstants.BUSINESS_ENTITY_REJECT_URL, businessCreationRejectUrl);
	    	execution.setVariable(ProcessContextParameterConstants.BUSINESS_NAME, businessName);
	    	execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT, ActionResultType.valid.toString());

		}
	}

}
