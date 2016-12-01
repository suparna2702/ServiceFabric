package com.similan.process.action.lead;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.lead.TransferLead;
import com.similan.domain.entity.lead.TransferLeadRequest;
import com.similan.domain.entity.lead.TransferState;
import com.similan.domain.repository.lead.TransferLeadRequestRepository;
import com.similan.framework.workflow.ProcessContextParameterConstants;

@Slf4j
@Component
public class LeadTransferCompleteValidationAction implements ActivityBehaviour {
	private static final long serialVersionUID = 854963596893531181L;
	
	@Autowired
	private TransferLeadRequestRepository transferLeadRequestRepository;

	public void execute(ActivityExecution execution) throws Exception {
		log.info("Starting LeadTransferCompleteValidationAction");
		// Look to see if all of the original leads have been processed
		TransferLeadRequest transferLeadRequest = getTransferLeadRequest(execution);

		boolean allLeadsAreProcessed = checkAllLeadsProcessed(transferLeadRequest);

		setExecutionParameters(execution, allLeadsAreProcessed);
		log.info("Finished LeadTransferCompleteValidationAction");
	}

	private void setExecutionParameters(ActivityExecution execution,
			boolean allLeadsAreProcessed) {
		if (allLeadsAreProcessed == true)
			execution.setVariable(
					ProcessContextParameterConstants.ACTION_RESULT, "complete");
		else
			execution.setVariable(
					ProcessContextParameterConstants.ACTION_RESULT,
					"not_complete");
	}

	private boolean checkAllLeadsProcessed(
			TransferLeadRequest transferLeadRequest) {
		boolean allLeadsAreProcessed = true;
		log.info("Transfer lead contains " + transferLeadRequest.getTransferLeads());
		for (TransferLead lead : transferLeadRequest.getTransferLeads()) {
			if (lead.getTransferState() == TransferState.Pending) {
				allLeadsAreProcessed = false;
				break;
			}
		}
		return allLeadsAreProcessed;
	}

	private TransferLeadRequest getTransferLeadRequest(
			ActivityExecution execution) {
		Long transferLeadRequestId = (Long) execution
				.getVariable(ProcessContextParameterConstants.TRANSFER_LEAD_TRANSFER_LEAD_REQUEST_ID);

		TransferLeadRequest transferLeadRequest = this.transferLeadRequestRepository
				.findOne(transferLeadRequestId);
		return transferLeadRequest;
	}
}
