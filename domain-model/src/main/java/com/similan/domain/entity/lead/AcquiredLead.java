package com.similan.domain.entity.lead;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity(name = "AcquiredLead")
@DiscriminatorValue("AcquiredLead")
public class AcquiredLead extends Lead {
	
	@Column(columnDefinition = "TINYINT(1)")
	private Boolean locationVerified;
	
	@Column(columnDefinition = "TINYINT(1)")
	private Boolean phoneVerified;
	
	@Column(columnDefinition = "TINYINT(1)")
	private Boolean nameVerified;
	
	@Column(columnDefinition = "TINYINT(1)")
	private Boolean intentVerified;
	
	@Column
	private Date infoLastVerifiedByOwner;
	
	@Column
	private Date infoLastVerifiedByBR;

	public Boolean getLocationVerified() {
		return locationVerified;
	}

	public void setLocationVerified(Boolean locationVerified) {
		this.locationVerified = locationVerified;
	}

	public Boolean getPhoneVerified() {
		return phoneVerified;
	}

	public void setPhoneVerified(Boolean phoneVerified) {
		this.phoneVerified = phoneVerified;
	}

	public Boolean getNameVerified() {
		return nameVerified;
	}

	public void setNameVerified(Boolean nameVerified) {
		this.nameVerified = nameVerified;
	}

	public Boolean getIntentVerified() {
		return intentVerified;
	}

	public void setIntentVerified(Boolean intentVerified) {
		this.intentVerified = intentVerified;
	}

	public Date getInfoLastVerifiedByOwner() {
		return infoLastVerifiedByOwner;
	}

	public void setInfoLastVerifiedByOwner(Date infoLastVerifiedByOwner) {
		this.infoLastVerifiedByOwner = infoLastVerifiedByOwner;
	}

	public Date getInfoLastVerifiedByBR() {
		return infoLastVerifiedByBR;
	}

	public void setInfoLastVerifiedByBR(Date infoLastVerifiedByBR) {
		this.infoLastVerifiedByBR = infoLastVerifiedByBR;
	}

}
