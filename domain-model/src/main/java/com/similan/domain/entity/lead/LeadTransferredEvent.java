package com.similan.domain.entity.lead;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity(name = "LeadTransferredEvent")
@DiscriminatorValue("LeadTransfered")
public class LeadTransferredEvent extends LeadLifeCycleEvent {

	@Column
	private Long fromOrgId;
	
	@Column
	private Long toOrgId;
	
	@ManyToOne
	private Lead parent;
	
	@Embedded
	private LeadTransferRecord leadTransferRecord;

	public LeadTransferRecord getLeadTransferRecord() {
		return leadTransferRecord;
	}

	public void setLeadTransferRecord(LeadTransferRecord leadTransferRecord) {
		this.leadTransferRecord = leadTransferRecord;
	}

	public Long getFromOrgId() {
		return fromOrgId;
	}

	public void setFromOrgId(Long fromOrgId) {
		this.fromOrgId = fromOrgId;
	}

	public Long getToOrgId() {
		return toOrgId;
	}

	public void setToOrgId(Long toOrgId) {
		this.toOrgId = toOrgId;
	}

	public Lead getParent() {
		return parent;
	}

	public void setParent(Lead parent) {
		this.parent = parent;
	}
	
}
