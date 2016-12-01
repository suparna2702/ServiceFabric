package com.similan.domain.entity.lead;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.similan.domain.common.YesNoEnum;
import com.similan.domain.entity.community.SocialPerson;

@Entity(name="LeadAlertSettings")
public class LeadAlertSettings {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column
	private YesNoEnum allValues;
	 
	@Enumerated(EnumType.STRING)
	@Column
	private YesNoEnum newLeadArrival;
	
	@Enumerated(EnumType.STRING)
	@Column
	private YesNoEnum newLeadTransfer;
	
	@Enumerated(EnumType.STRING)
	@Column
	private YesNoEnum memberLeadUpdate;
	 
	@Enumerated(EnumType.STRING)
	@Column
	private YesNoEnum alertViaEmail;
	 
	@Enumerated(EnumType.STRING)
	@Column
	private YesNoEnum alertViaSms;
	
	@ManyToOne
	private SocialPerson owner;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public YesNoEnum getAllValues() {
		return allValues;
	}

	public void setAllValues(YesNoEnum allValues) {
		this.allValues = allValues;
	}

	public YesNoEnum getNewLeadArrival() {
		return newLeadArrival;
	}

	public void setNewLeadArrival(YesNoEnum newLeadArrival) {
		this.newLeadArrival = newLeadArrival;
	}

	public YesNoEnum getNewLeadTransfer() {
		return newLeadTransfer;
	}

	public void setNewLeadTransfer(YesNoEnum newLeadTransfer) {
		this.newLeadTransfer = newLeadTransfer;
	}

	public YesNoEnum getMemberLeadUpdate() {
		return memberLeadUpdate;
	}

	public void setMemberLeadUpdate(YesNoEnum memberLeadUpdate) {
		this.memberLeadUpdate = memberLeadUpdate;
	}

	public YesNoEnum getAlertViaEmail() {
		return alertViaEmail;
	}

	public void setAlertViaEmail(YesNoEnum alertViaEmail) {
		this.alertViaEmail = alertViaEmail;
	}

	public YesNoEnum getAlertViaSms() {
		return alertViaSms;
	}

	public void setAlertViaSms(YesNoEnum alertViaSms) {
		this.alertViaSms = alertViaSms;
	}

	public SocialPerson getOwner() {
		return owner;
	}

	public void setOwner(SocialPerson owner) {
		this.owner = owner;
	}
	
	

}
