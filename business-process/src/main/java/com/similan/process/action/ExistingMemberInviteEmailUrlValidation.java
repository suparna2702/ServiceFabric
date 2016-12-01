package com.similan.process.action;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.stereotype.Component;

import com.similan.framework.workflow.ProcessContextParameterConstants;

@Slf4j
@Component
public class ExistingMemberInviteEmailUrlValidation implements
		ActivityBehaviour {

	private static final long serialVersionUID = 1L;

	public void execute(ActivityExecution execution) throws Exception {
		log.info("Start ExistingMemberInviteEmailUrlValidation");
		String decision = (String) execution
				.getVariable(ProcessContextParameterConstants.MEMBER_INVITE_DECISION);

		if (decision.equals("Accepted"))
			execution.setVariable(
					ProcessContextParameterConstants.ACTION_RESULT, "accepted");
		else
			execution.setVariable(
					ProcessContextParameterConstants.ACTION_RESULT, "rejected");
		log.info("Finished ExistingMemberInviteEmailUrlValidation");

	}
}
