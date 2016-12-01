package com.similan.process.action.lead;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.lead.TransferLeadRequest;
import com.similan.domain.repository.lead.TransferLeadRequestRepository;
import com.similan.framework.workflow.ProcessContextParameterConstants;

@Slf4j
@Component
public class LeadTransferUpdateProcessIdAction implements ActivityBehaviour {
	private static final long serialVersionUID = -1908011987348434991L;

	@Autowired
	private TransferLeadRequestRepository transferLeadRequestRepository;

	public void execute(ActivityExecution execution) throws Exception { 
		log.info("Starting LeadTransferUpdateProcessIdAction");

		TransferLeadRequest transferLeadRequest = getTransferLeadRequest(execution);

		log.info("Updating process ID to " + execution.getId() + " for transfer request ID " + transferLeadRequest.getId() );
		
		transferLeadRequest.setWorkflowProcessId(execution.getId());
		transferLeadRequestRepository.save(transferLeadRequest);
		
		log.info("Finished LeadTransferUpdateProcessIdAction");
	}

	private TransferLeadRequest getTransferLeadRequest(
			ActivityExecution execution) {
		Long transferLeadRequestId = (Long) execution
		.getVariable(ProcessContextParameterConstants.TRANSFER_LEAD_TRANSFER_LEAD_REQUEST_ID);

		TransferLeadRequest transferLeadRequest = this.transferLeadRequestRepository.findOne(transferLeadRequestId);
		return transferLeadRequest;
	}
}
