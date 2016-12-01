package com.similan.domain.entity.partner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="PartnerProgramBenifit")
public class PartnerProgramBenifit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=1000)
	private String benifitName;
	
	@Column(length=8000)
	private String benifitDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBenifitName() {
		return benifitName;
	}

	public void setBenifitName(String benifitName) {
		this.benifitName = benifitName;
	}

	public String getBenifitDetails() {
		return benifitDetails;
	}

	public void setBenifitDetails(String benifitDetails) {
		this.benifitDetails = benifitDetails;
	}
	
	

}
