package com.similan.domain.entity.lead;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "LeadCrmSynchronizationEvent")
@DiscriminatorValue("LeadCrmSynchronization")
public class LeadCrmSynchronizationEvent extends LeadLifeCycleEvent {

	@Column
	private Long orgId;

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
}
