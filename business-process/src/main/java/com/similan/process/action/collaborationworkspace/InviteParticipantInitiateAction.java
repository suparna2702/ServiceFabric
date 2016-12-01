package com.similan.process.action.collaborationworkspace;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceInvite;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceInviteRepository;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.service.api.community.SocialActorService;

@Slf4j
@Component
public class InviteParticipantInitiateAction implements ActivityBehaviour {
	private static final long serialVersionUID = -1908011987348434991L;

	@Autowired
	private PlatformCommonSettings platformCommonSettings;
	@Autowired
	private CollaborationWorkspaceRepository collaborationWorkspaceRepository;
	@Autowired
	private SocialPersonRepository socialPersonRepository;
	@Autowired
	private SocialActorService socialActorService;
	@Autowired
	private CollaborationWorkspaceInviteRepository collaborationWorkspaceInviteRepository;

	public void execute(ActivityExecution execution) throws Exception {
		
		  log.info("Starting InviteParticipantInitiateAction ID:"
					+ execution.getId());
		  
	      Long workspaceId = (Long)execution
	    		    .getVariable(
	    		            ProcessContextParameterConstants.COLLAB_WORKSPACE_ID);
	      log.info("Workspace id " + workspaceId);
		  
		  Long invteReqId = (Long)execution
		  		    .getVariable(
				            ProcessContextParameterConstants.COLLAB_WORKSPACE_INVITE_REQUEST_ID);
		  log.info("Invite request id " + invteReqId);
		  
		  Long inviterId = (Long)execution
	  		    .getVariable(
			            ProcessContextParameterConstants.COLLAB_WORKSPACE_INVITER_ID);
		  log.info("Inviter id " + inviterId);
		  
		  Long inviteeId = (Long)execution
		  		    .getVariable(
				            ProcessContextParameterConstants.COLLAB_WORKSPACE_INVITEE_ID);
		  log.info("Invitee id " + inviteeId);
		  CollaborationWorkspaceInvite invite = collaborationWorkspaceInviteRepository.findOne(invteReqId);
		  setExecutionParameters(execution, invite);
	
		  log.info("Finished InviteParticipantInitiateAction");
	}

	private void setExecutionParameters(ActivityExecution execution,
			CollaborationWorkspaceInvite request) {

		/* Put everything for sending the approval request email */
		String inviteeFirstName = StringUtils.EMPTY;
		String inviteeLastName = StringUtils.EMPTY;
		
		if(request.getInvitee() instanceof SocialPerson){
			inviteeFirstName = ((SocialPerson) request.getInvitee()).getFirstName();
			inviteeLastName = ((SocialPerson) request.getInvitee()).getLastName();
		}
		else if(request.getInvitee() instanceof SocialOrganization){
			inviteeFirstName = ((SocialOrganization) request.getInvitee()).getBusinessName();
			inviteeLastName = ((SocialOrganization) request.getInvitee()).getBusinessName();
		}

		execution
				.setVariable(
						ProcessContextParameterConstants.COLLAB_WORKSPACE_INVITEE_FIRSTNAME,
						StringUtils.defaultIfEmpty(inviteeFirstName, ""));
		execution
				.setVariable(
						ProcessContextParameterConstants.COLLAB_WORKSPACE_INVITEE_LASTNAME,
						StringUtils.defaultIfEmpty(inviteeLastName, ""));

		execution
				.setVariable(
						ProcessContextParameterConstants.COLLAB_WORKSPACE_INVITER_FIRSTNAME,
						StringUtils.defaultIfEmpty(((SocialPerson) request
								.getInviter()).getFirstName(), ""));
		execution
				.setVariable(
						ProcessContextParameterConstants.COLLAB_WORKSPACE_INVITER_LASTNAME,
						StringUtils.defaultIfEmpty(((SocialPerson) request
								.getInviter()).getLastName(), ""));

		execution.setVariable(
				ProcessContextParameterConstants.COLLAB_WORSKACE_NAME,
				request.getWorkspace().getName());
		
		String workspaceOwnerName = request.getWorkspace().getOwner().getName();
		String workspaceName = request.getWorkspace().getName();
	    String inviteeName = request.getInvitee().getName();
	    
		StringBuilder acceptBaseUrl = new StringBuilder();
		acceptBaseUrl.append(platformCommonSettings
						.getPortalApplcationUrl().getValue())
						.append("collabspace/acceptWorkspaceInvitation.xhtml?wsowner=")
						.append(workspaceOwnerName)
						.append("&wsname=")
						.append(workspaceName)
						.append("&invitee=")
						.append(inviteeName)
						.append("&result=").append("accept");

		execution
				.setVariable(
						ProcessContextParameterConstants.COLLAB_WORKSPACE_ACCEPT_BASE_URL,
						StringUtils.defaultIfEmpty(acceptBaseUrl.toString(), ""));
		
	    
		StringBuilder rejectBaseUrl = new StringBuilder();
		rejectBaseUrl.append(platformCommonSettings
				.getPortalApplcationUrl().getValue())
				.append("collabspace/acceptWorkspaceInvitation.xhtml?wsowner=")
				.append(workspaceOwnerName)
				.append("&wsname=")
				.append(workspaceName)
				.append("&invitee=")
				.append(inviteeName)
				.append("&result=").append("reject");

		execution
				.setVariable(
						ProcessContextParameterConstants.COLLAB_WORKSPACE_REJECT_BASE_URL,
						StringUtils.defaultIfEmpty(rejectBaseUrl.toString(), ""));

		execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
				getEmailToAddress(request.getInvitee()));
	}

	private Object getEmailToAddress(SocialActor socialActor) {
		String emailAddress = null;
		if(socialActor instanceof SocialPerson){
			emailAddress = ((SocialPerson) socialActor).getPrimaryEmail();
		}
		else if(socialActor instanceof SocialOrganization){
			emailAddress = ((SocialOrganization) socialActor).getPrimaryEmail();
		}
		

		return emailAddress;
	}
}
