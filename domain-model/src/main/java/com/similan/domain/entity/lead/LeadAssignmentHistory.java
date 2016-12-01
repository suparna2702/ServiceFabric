package com.similan.domain.entity.lead;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="LeadAssignmentHistory")
public class LeadAssignmentHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private Long connectedLead;
	
	@Column
	private Long assignedTo;
	
	@Column
	private Date whenAssigned;
	
	@Column
	private Date whenEnded;
	
	@Enumerated(EnumType.STRING)
	@Column
	private LeadType leadType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getConnectedLead() {
		return connectedLead;
	}

	public void setConnectedLead(Long connectedLead) {
		this.connectedLead = connectedLead;
	}

	public Long getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Long assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Date getWhenAssigned() {
		return whenAssigned;
	}

	public void setWhenAssigned(Date whenAssigned) {
		this.whenAssigned = whenAssigned;
	}

	public Date getWhenEnded() {
		return whenEnded;
	}

	public void setWhenEnded(Date whenEnded) {
		this.whenEnded = whenEnded;
	}

	public LeadType getLeadType() {
		return leadType;
	}

	public void setLeadType(LeadType leadType) {
		this.leadType = leadType;
	}
	
    
}
