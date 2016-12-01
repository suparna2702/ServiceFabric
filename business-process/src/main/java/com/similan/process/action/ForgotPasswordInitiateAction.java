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

public class ForgotPasswordInitiateAction implements ActivityBehaviour {
	private static final long serialVersionUID = 1L;

	@Autowired
	private SocialPersonRepository personRepository;
	@Autowired
	private WorkflowTransientStateRepository memberTransientStateRepository;
	@Autowired
	private PlatformCommonSettings platformCommonSettings;

	public void execute(ActivityExecution execution) throws Exception {

		/* Get the parameters */
		Long memberId = (Long) execution
				.getVariable(ProcessContextParameterConstants.MEMBER_ID);
		if (memberId != null) {

			/* get the member */
			SocialPerson member = this.personRepository.findOne(memberId);

			if (member != null) {

				StringBuilder urlBuilder = new StringBuilder();
				String hostUrl = platformCommonSettings
						.getPortalApplcationUrl().getValue();
				String processInstanceId = execution.getId();

				WorkflowTransientState memberTransientState = this.memberTransientStateRepository.create();
				memberTransientState.setMemberId(member.getId());
				memberTransientState.setProcessInstanceId(processInstanceId);
				memberTransientState.setStateType(WorkflowTransientStateType.ForgotPassword);
				
				WorkflowTransientStateAttribute attribute = this.memberTransientStateRepository.createAttribute();
				attribute.setAttributeName(AttributeConstantUtil.MEMBER_FORGOT_PASSWORD_KEY);
				attribute.setAttributeType(AttributeConstantUtil.ATTRIBUTE_TYPE_STRING);
				attribute.setAttributeValue(processInstanceId);
				
				List<WorkflowTransientStateAttribute> attributes = new ArrayList<WorkflowTransientStateAttribute>();
				attributes.add(attribute);
				memberTransientState.setAttributes(attributes);
				this.memberTransientStateRepository.save(memberTransientState);
				
				
				
				urlBuilder.append(hostUrl)
						.append("about/forgotPasswordChangeInput.xhtml?mid=")
						.append(member.getId()).append("&pid=")
						.append(processInstanceId);
				String forgotPasswordResetUrl = urlBuilder.toString();

				/* Put everything in the context */
				execution
						.setVariable(
								ProcessContextParameterConstants.FORGOT_PASSWORD_RESET_URL,
								forgotPasswordResetUrl);
				execution.setVariable(ProcessContextParameterConstants.EMAIL,
						member.getPrimaryEmail());
				execution.setVariable(
						ProcessContextParameterConstants.TO_EMAIL,
						member.getPrimaryEmail());
				execution.setVariable(
						ProcessContextParameterConstants.MEMBER_FIRSTNAME,
						member.getFirstName());
				execution.setVariable(
						ProcessContextParameterConstants.ACTION_RESULT,
						ActionResultType.valid.toString());
			}
		}
	}
}
