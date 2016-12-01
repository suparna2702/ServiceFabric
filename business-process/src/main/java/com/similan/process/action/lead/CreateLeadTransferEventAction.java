package com.similan.process.action.lead;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.lead.TransferLead;
import com.similan.domain.repository.lead.TransferLeadRepository;
import com.similan.framework.workflow.ProcessContextParameterConstants;

@Slf4j
@Component
public class CreateLeadTransferEventAction implements ActivityBehaviour {

	private static final long serialVersionUID = -2955474997558366205L;


	@Autowired
	private TransferLeadRepository transferLeadRepository;

	public void execute(ActivityExecution execution) throws Exception {
		log.info("Starting CreateLeadTransferEventAction");
		
		// Get Transfer Lead
		getTransferLead(execution); 

		log.info("Finished CreateLeadTransferEventAction");
	}

	private TransferLead getTransferLead(ActivityExecution execution) {
		Long transferLeadId = (Long) execution
				.getVariable(ProcessContextParameterConstants.TRANSFER_LEAD_ID);

		TransferLead transferLead = transferLeadRepository
				.findOne(transferLeadId);
		return transferLead;
	}
}
