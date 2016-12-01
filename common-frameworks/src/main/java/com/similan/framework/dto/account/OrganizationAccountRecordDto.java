package com.similan.framework.dto.account;

import java.io.Serializable;
import java.util.Date;

import com.similan.domain.entity.acccount.AccountRecordState;
import com.similan.domain.entity.acccount.AccountRecordType;
import com.similan.framework.dto.lead.AffiliateLeadDto;
import com.similan.framework.dto.lead.LeadDto;

public class OrganizationAccountRecordDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private AccountRecordState recordState;
	
	private float transactionAmount;
	
	private Date timeStamp;
	
	private AccountRecordType recordType;
	
	private AffiliateLeadDto affiliateLead;
	
	private LeadDto acquiredLead;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AccountRecordState getRecordState() {
		return recordState;
	}

	public void setRecordState(AccountRecordState recordState) {
		this.recordState = recordState;
	}

	public float getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(float transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public AccountRecordType getRecordType() {
		return recordType;
	}

	public void setRecordType(AccountRecordType recordType) {
		this.recordType = recordType;
	}

	public AffiliateLeadDto getAffiliateLead() {
		return affiliateLead;
	}

	public void setAffiliateLead(AffiliateLeadDto affiliateLead) {
		this.affiliateLead = affiliateLead;
	}

	public LeadDto getAcquiredLead() {
		return acquiredLead;
	}

	public void setAcquiredLead(LeadDto acquiredLead) {
		this.acquiredLead = acquiredLead;
	}
}
