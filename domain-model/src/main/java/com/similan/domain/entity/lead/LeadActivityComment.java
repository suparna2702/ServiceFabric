package com.similan.domain.entity.lead;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="LeadActivityComment")
public class LeadActivityComment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
	private Long id;
	
	@Embedded
	private LeadNote leadNote;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LeadNote getLeadNote() {
		return leadNote;
	}

	public void setLeadNote(LeadNote leadNote) {
		this.leadNote = leadNote;
	}

}
