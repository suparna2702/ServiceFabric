package com.similan.domain.entity.lead;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "LeadImportedEvent")
@DiscriminatorValue("LeadImported")
public class LeadImportedEvent extends LeadLifeCycleEvent {

	@Column
	private Long orgId;
	
	@Column
	private int numberOfLeadsImported;

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public int getNumberOfLeadsImported() {
		return numberOfLeadsImported;
	}

	public void setNumberOfLeadsImported(int numberOfLeadsImported) {
		this.numberOfLeadsImported = numberOfLeadsImported;
	}
	
}
