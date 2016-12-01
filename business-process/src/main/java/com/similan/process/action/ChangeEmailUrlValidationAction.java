package com.similan.process.action;

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
import com.similan.framework.workflow.ProcessContextParameterConstants;

public class ChangeEmailUrlValidationAction implements ActivityBehaviour {
	private static final long serialVersionUID = 1L;
	@Autowired
	private SocialPersonRepository personRepository;
	@Autowired
	private WorkflowTransientStateRepository memberTransientStateRepository;

	public void execute(ActivityExecution execution) throws Exception {
		Long memberId = (Long)execution.getVariable(ProcessContextParameterConstants.MEMBER_ID);
		
		if(memberId != null){
			
			SocialPerson member = this.personRepository.findOne(memberId);
			WorkflowTransientState tranState = memberTransientStateRepository
					                           .findByMemberIdAndProcessInstanceId(memberId, execution.getId());
			
			if(tranState != null && tranState.getStateType() == WorkflowTransientStateType.EmailChange){
				List<WorkflowTransientStateAttribute> attrList = tranState.getAttributes();
				String newEmail = null;
				
				for(WorkflowTransientStateAttribute attr : attrList){
					if(attr.getAttributeName().contentEquals(AttributeConstantUtil.MEMBER_NEW_EMAIL) == true){
						newEmail = attr.getAttributeValue();
						break;
					}
				}

				if(newEmail != null){
					
					execution.setVariable(ProcessContextParameterConstants.MEMBER_OLD_EMAIL, 
				                                   member.getPrimaryEmail());
					execution.setVariable(ProcessContextParameterConstants.MEMBER_CHANGE_NEW_EMAIL, 
							newEmail);

					member.setPrimaryEmail(newEmail);
					this.personRepository.save(member);
					execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT, 
				              ActionResultType.valid.toString());
					this.memberTransientStateRepository.delete(tranState);

				}
				else {
					execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT, 
				              ActionResultType.inValid.toString());
					
				}
			}
			else {
				execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT, 
			              ActionResultType.inValid.toString());
			}
			
		}
	}

}
