package com.similan.domain.entity.acccount;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.lead.AcquiredLead;
import com.similan.domain.entity.lead.AffiliateLead;

@Entity(name = "LeadPurchaseRecord")
@DiscriminatorValue("LeadPurchaseRecord")
public class LeadPurchaseRecord extends AccountRecord {
	
	@ManyToOne
	private AffiliateLead purchasedFrom;
	
	@ManyToOne
	private AcquiredLead purchasedLead;

	public AffiliateLead getPurchasedFrom() {
		return purchasedFrom;
	}

	public void setPurchasedFrom(AffiliateLead purchasedFrom) {
		this.purchasedFrom = purchasedFrom;
	}

	public AcquiredLead getPurchasedLead() {
		return purchasedLead;
	}

	public void setPurchasedLead(AcquiredLead purchasedLead) {
		this.purchasedLead = purchasedLead;
	}

}
