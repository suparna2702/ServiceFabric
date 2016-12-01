package com.similan.framework.dto.lead;

import java.io.Serializable;
import java.util.Date;

public class LeadAssignmentDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private LeadDto theLead;
	
	private Date whenAssigned;
	
	private String description;

	public LeadDto getTheLead() {
		return theLead;
	}

	public void setTheLead(LeadDto theLead) {
		this.theLead = theLead;
	}

	public Date getWhenAssigned() {
		return whenAssigned;
	}

	public void setWhenAssigned(Date whenAssigned) {
		this.whenAssigned = whenAssigned;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
