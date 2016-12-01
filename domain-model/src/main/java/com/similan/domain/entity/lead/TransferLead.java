package com.similan.domain.entity.lead;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity(name = "TransferLead")
@DiscriminatorValue("TransferLead")
public class TransferLead extends Lead {
		
	@ManyToOne
	private Lead parentLead;
	

	@Enumerated(EnumType.STRING)
	@Column
	private TransferState transferState;

	@Column
	private String processId;
	
	@ManyToOne
	private TransferLeadRequest transferLeadRequest;
	
	public Lead getParentLead() {
		return parentLead;
	}

	public void setParentLead(Lead parentLead) {
		this.parentLead = parentLead;
	}

	public TransferState getTransferState() {
		return transferState;
	}

	public void setTransferState(TransferState transferState) {
		this.transferState = transferState;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public TransferLeadRequest getTransferLeadRequest() {
		return transferLeadRequest;
	}

	public void setTransferLeadRequest(TransferLeadRequest transferLeadRequest) {
		this.transferLeadRequest = transferLeadRequest;
	}
}
