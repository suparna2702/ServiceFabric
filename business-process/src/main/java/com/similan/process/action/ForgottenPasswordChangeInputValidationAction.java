package com.similan.process.action;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.workflow.ProcessContextParameterConstants;

public class ForgottenPasswordChangeInputValidationAction implements
		ActivityBehaviour {
	private static final long serialVersionUID = 1L;

	@Autowired
	private SocialPersonRepository personRepository;
	@Autowired
	private WorkflowTransientStateRepository memberTransientStateRepository;

	public void execute(ActivityExecution execution) throws Exception {

		/* Get the parameters */
		String newPassword = (String) execution
				.getVariable(ProcessContextParameterConstants.MEMBER_CHANGE_PASSWORD);
		Long memberId = (Long) execution
				.getVariable(ProcessContextParameterConstants.MEMBER_ID);

		/* get the member */
		SocialPerson member = this.personRepository.findOne(memberId);

		if (newPassword != null) {

			member.getMemberAccount().setPassword(newPassword);
			this.personRepository.save(member);

			removeTransientUrlKey(execution, memberId);

			/* Put everything in the context */
			execution.setVariable(
					ProcessContextParameterConstants.MEMBER_CHANGE_NEW_EMAIL,
					newPassword);
			execution.setVariable(ProcessContextParameterConstants.EMAIL,
					member.getPrimaryEmail());
			execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
					member.getPrimaryEmail());
			execution.setVariable(
					ProcessContextParameterConstants.MEMBER_FIRSTNAME,
					member.getFirstName());
			execution.setVariable(
					ProcessContextParameterConstants.ACTION_RESULT,
					ActionResultType.valid.toString());
		} else {
			execution.setVariable(
					ProcessContextParameterConstants.ACTION_RESULT,
					ActionResultType.inValid.toString());
		}

	}

	private void removeTransientUrlKey(ActivityExecution execution,
			Long memberId) {
		// Clean up transit state repo
		WorkflowTransientState tranState = memberTransientStateRepository
				.findByMemberIdAndProcessInstanceId(memberId,
						execution.getId());

		if (tranState != null) {
			memberTransientStateRepository.delete(tranState);
		}
	}
}
