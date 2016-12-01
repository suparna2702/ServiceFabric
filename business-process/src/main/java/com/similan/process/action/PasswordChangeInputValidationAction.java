package com.similan.process.action;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.workflow.ProcessContextParameterConstants;

@Component
public class PasswordChangeInputValidationAction implements ActivityBehaviour {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SocialPersonRepository personRepository;
	@Autowired
	private WorkflowTransientStateRepository memberTransientStateRepository;
	@Autowired
	private PlatformCommonSettings platformCommonSettings;
	
	public void execute(ActivityExecution execution) throws Exception {
		
		/* Get the parameters */
		String newPassword = (String) execution
				.getVariable(ProcessContextParameterConstants.MEMBER_CHANGE_PASSWORD);
		Long memberId = (Long) execution
				.getVariable(ProcessContextParameterConstants.MEMBER_ID);

		/* get the member */
		SocialPerson member = this.personRepository.findOne(memberId);

		if (newPassword != null) {

			execution.setVariable(ProcessContextParameterConstants.EMAIL,
					member.getPrimaryEmail());
			member.getMemberAccount().setPassword(newPassword);
			this.personRepository.save(member);

			/* Put everything in the context */
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

}
