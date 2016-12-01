package com.similan.framework.dto.lead;

import java.io.Serializable;
import java.util.Date;

import com.similan.domain.entity.lead.TransferState;

public class LeadTransferStatusDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private Long id;
	
	
    private TransferState transferState;
    
    private String transfereeName;
    
    private Date dateCreated = new Date();
    
	public LeadTransferStatusDto(){
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public TransferState getTransferState() {
		return transferState;
	}

	public void setTransferState(TransferState transferState) {
		this.transferState = transferState;
	}

	public String getTransfereeName() {
		return transfereeName;
	}

	public void setTransfereeName(String transfereeName) {
		this.transfereeName = transfereeName;
	}
}
