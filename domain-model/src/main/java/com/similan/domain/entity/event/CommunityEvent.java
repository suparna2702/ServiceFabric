package com.similan.domain.entity.event;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity(name="Event")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "CommunityEventType", 
                discriminatorType = DiscriminatorType.STRING)
public class CommunityEvent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
	private Long id;
	
	@Column
	private Long eventGenerator;
	
	@Column
	private Date eventGenerationTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventGenerator() {
		return eventGenerator;
	}

	public void setEventGenerator(Long eventGenerator) {
		this.eventGenerator = eventGenerator;
	}

	public Date getEventGenerationTime() {
		return eventGenerationTime;
	}

	public void setEventGenerationTime(Date eventGenerationTime) {
		this.eventGenerationTime = eventGenerationTime;
	}
}
