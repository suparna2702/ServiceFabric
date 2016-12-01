package com.similan.process.action;
 
import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.workflow.ProcessContextParameterConstants;

@Slf4j
@Component
public class ExistingMemberRejectedInviteEmailUrlValidation implements ActivityBehaviour {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SocialPersonRepository personRepository;
	@Autowired
	private WorkflowTransientStateRepository memberTransientStateRepository;

	public void execute(ActivityExecution execution) throws Exception {
		Long memberId = (Long)execution.getVariable(ProcessContextParameterConstants.MEMBER_ID);
		log.info("Member to be validated " + memberId);
		
		if(memberId != null){
			
			SocialPerson member = this.personRepository.findOne(memberId);
			log.info("Social person to be validated " + member.getFirstName());
			
			WorkflowTransientState tranState = memberTransientStateRepository
                                    .findByMemberIdAndProcessInstanceId(memberId, execution.getId());

            if(tranState != null) {
               	Long inviterId = Long.valueOf(tranState.getAttributeByName(ProcessContextParameterConstants.INVITER_ID).getAttributeValue());
            	String inviteeFirstName = tranState.getAttributeByName(ProcessContextParameterConstants.INVITEE_FIRSTNAME).getAttributeValue();
            	String inviteeLastName = tranState.getAttributeByName(ProcessContextParameterConstants.INVITEE_LASTNAME).getAttributeValue();

            	log.info("Member is joining by invite process " + memberId);
				SocialPerson inviter = this.personRepository.findOne(inviterId);
				
				
				memberTransientStateRepository.delete(tranState);
            	
        		execution.setVariable(
        				ProcessContextParameterConstants.INVITEE_FIRSTNAME,
        				inviteeFirstName);
        		execution.setVariable(ProcessContextParameterConstants.INVITEE_LASTNAME,
        				inviteeLastName);
        		execution.setVariable(
        				ProcessContextParameterConstants.MEMBER_FIRSTNAME,
        				inviter.getFirstName());
        		execution.setVariable(ProcessContextParameterConstants.MEMBER_LASTNAME,
        				inviter.getLastName());
            }
		}
	}
}
