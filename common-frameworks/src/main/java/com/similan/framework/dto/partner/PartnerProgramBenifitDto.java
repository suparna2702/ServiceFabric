package com.similan.framework.dto.partner;

import java.io.Serializable;

public class PartnerProgramBenifitDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id = Long.MIN_VALUE;
	
	private String benifitName;
	
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

	@Override
	public String toString() {
		return "PartnerProgramBenifitDto [id=" + id + ", benifitName="
				+ benifitName + ", benifitDetails=" + benifitDetails + "]";
	}
	
	
	
	

}
