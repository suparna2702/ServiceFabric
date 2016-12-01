package com.similan.process.action.collaborationworkspace;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceParticipation;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceParticipationRepository;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.service.api.community.SocialActorService;

@Slf4j
@Component
public class InviteParticipantApprovedAction implements ActivityBehaviour {
	private static final long serialVersionUID = -1908011987348434991L;

	@Autowired
	private CollaborationWorkspaceRepository collaborationWorkspaceRepository;
	@Autowired
	private SocialPersonRepository socialPersonRepository;
	@Autowired
	private SocialActorService socialActorService;
	@Autowired
	private CollaborationWorkspaceParticipationRepository collaborationWorkspaceParticipationRepository;

	public void execute(ActivityExecution execution) throws Exception {
		log.info("Starting InviteParticipantApprovedAction ID:"
				+ execution.getId());
		
		if(this.isApproved(execution)){
			SocialActor invitee = getInvitee(execution);
			CollaborationWorkspace workspace = getCollaborationWorkspace(execution);

			CollaborationWorkspaceParticipation collaborationWorkspaceParticipation = collaborationWorkspaceParticipationRepository
			    .create(workspace, invitee, new Date());
			collaborationWorkspaceParticipationRepository
					.save(collaborationWorkspaceParticipation);

			log.info("Finished InviteParticipantApprovedAction");
		}
	}

	private SocialActor getInvitee(ActivityExecution execution) {

		Long id = (Long) execution
				.getVariable(ProcessContextParameterConstants.COLLAB_WORKSPACE_INVITEE_ID);

		SocialPerson invitee = socialPersonRepository.findOne(id);

		return invitee;
	}
	
	private Boolean isApproved(ActivityExecution execution){
		Boolean result = (Boolean) execution
				.getVariable(ProcessContextParameterConstants.COLLAB_WORKSPACE_INVITE_RESULT);
		log.info("Approval ststus " + result);
		return result;
	}

	private CollaborationWorkspace getCollaborationWorkspace(
			ActivityExecution execution) {
		Long id = (Long) execution
				.getVariable(ProcessContextParameterConstants.COLLAB_WORKSPACE_ID);

		CollaborationWorkspace collaborationWorkspace = collaborationWorkspaceRepository
				.findOne(id);
		return collaborationWorkspace;
	}
}
