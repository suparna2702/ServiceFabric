package com.similan.process.action.lead;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.lead.TransferLead;
import com.similan.domain.entity.lead.TransferLeadRequest;
import com.similan.domain.entity.lead.TransferState;
import com.similan.domain.repository.lead.TransferLeadRepository;
import com.similan.domain.repository.lead.TransferLeadRequestRepository;
import com.similan.framework.workflow.ProcessContextParameterConstants;

@Slf4j
@Component
public class LeadTransferExpiredAction implements ActivityBehaviour {

	private static final long serialVersionUID = -1908011987348434991L;


	@Autowired
	private TransferLeadRequestRepository transferLeadRequestRepository;
	@Autowired
	private TransferLeadRepository transferLeadRepository;

	public void execute(ActivityExecution execution) throws Exception {
		log.info("Starting LeadTransferExpiredAction");
		TransferLeadRequest transferLeadRequest = getTransferLeadRequest(execution);

		expireAllPendingTransferLeads(transferLeadRequest);

		saveTransferLeadRequest(transferLeadRequest);
		log.info("Finished LeadTransferExpiredAction");
	}

	private void saveTransferLeadRequest(TransferLeadRequest transferLeadRequest) {
		transferLeadRequestRepository.save(transferLeadRequest);
	}

	private void expireAllPendingTransferLeads(
			TransferLeadRequest transferLeadRequest) {
		for (TransferLead transferLead : transferLeadRequest.getTransferLeads()) {
			if (transferLead.getTransferState() == TransferState.Pending) {
				transferLead.setTransferState(TransferState.Expired);
				this.transferLeadRepository.save(transferLead);}
		}
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
