package com.similan.domain.entity.lead;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="LeadAssignment")
public class LeadAssignment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private Long toAssigned;
	
	@Column
	private Date whenAssigned;
	
	@Column
	private String description;
	
	@Column
	private Long associatedLead;
	
	@Enumerated(EnumType.ORDINAL)
	@Column
	private LeadType leadType;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAssociatedLead() {
		return associatedLead;
	}

	public void setAssociatedLead(Long associatedLead) {
		this.associatedLead = associatedLead;
	}

	public Long getToAssigned() {
		return toAssigned;
	}

	public void setToAssigned(Long toAssigned) {
		this.toAssigned = toAssigned;
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

	public LeadType getLeadType() {
		return leadType;
	}

	public void setLeadType(LeadType leadType) {
		this.leadType = leadType;
	}

}
