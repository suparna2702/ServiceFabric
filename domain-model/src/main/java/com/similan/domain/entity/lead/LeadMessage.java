package com.similan.domain.entity.lead;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.community.SocialPerson;

@Entity(name="LeadMessage")
public class LeadMessage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@ManyToOne
	private Lead owner;
	
	@Embedded
	private LeadNote leadNote;
	
	@ManyToOne
	private SocialPerson commenter;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public SocialPerson getCommenter() {
		return commenter;
	}

	public void setCommenter(SocialPerson commenter) {
		this.commenter = commenter;
	}

	public Lead getOwner() {
		return owner;
	}

	public void setOwner(Lead owner) {
		this.owner = owner;
	}

	public LeadNote getLeadNote() {
		return leadNote;
	}

	public void setLeadNote(LeadNote leadNote) {
		this.leadNote = leadNote;
	}

}
