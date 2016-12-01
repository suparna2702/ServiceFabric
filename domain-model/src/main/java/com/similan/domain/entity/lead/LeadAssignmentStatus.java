package com.similan.domain.entity.lead;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="LeadAssignmentStatusType")
public class LeadAssignmentStatus {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private Date statusChangeTime;
	
	@Enumerated(EnumType.STRING)
	@Column
	private LeadAssignmentStatusType changedStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStatusChangeTime() {
		return statusChangeTime;
	}

	public void setStatusChangeTime(Date statusChangeTime) {
		this.statusChangeTime = statusChangeTime;
	}

	public LeadAssignmentStatusType getChangedStatus() {
		return changedStatus;
	}

	public void setChangedStatus(LeadAssignmentStatusType changedStatus) {
		this.changedStatus = changedStatus;
	}
}
