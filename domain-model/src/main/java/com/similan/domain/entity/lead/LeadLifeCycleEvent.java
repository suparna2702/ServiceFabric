package com.similan.domain.entity.lead;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity(name = "LeadLifeCycleEvent")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "LifecycleDiscriminatorType", discriminatorType = DiscriminatorType.STRING)
public class LeadLifeCycleEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
	private Long id;
		
	@Enumerated(EnumType.STRING)
	@Column
	LeadLifeCycleState leadLifecycleState;

	@Column
	private Date timeStamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public LeadLifeCycleState getLeadLifecycleState() {
		return leadLifecycleState;
	}

	public void setLeadLifecycleState(LeadLifeCycleState leadLifecycleState) {
		this.leadLifecycleState = leadLifecycleState;
	}
}
