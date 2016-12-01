package com.similan.domain.entity.lead;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.leadcapture.LeadCaptureResponse;

@Entity(name = "ContactLead")
@DiscriminatorValue("ContactLead")
public class ContactLead extends Lead {
	
	@Embedded
	private ContactMessageDetail contactMessageDetail;
	
	@ManyToOne
	private LeadCaptureResponse associatedResponse;
	
	public LeadCaptureResponse getAssociatedResponse() {
		return associatedResponse;
	}

	public void setAssociatedResponse(LeadCaptureResponse associatedResponse) {
		this.associatedResponse = associatedResponse;
	}

	public ContactMessageDetail getContactMessageDetail() {
		return contactMessageDetail;
	}

	public void setContactMessageDetail(ContactMessageDetail contactMessageDetail) {
		this.contactMessageDetail = contactMessageDetail;
	}
}
