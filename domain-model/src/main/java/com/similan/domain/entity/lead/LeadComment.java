package com.similan.domain.entity.lead;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.similan.domain.entity.community.SocialPerson;

@Entity(name="LeadComment")
public class LeadComment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<LeadComment> childComments;
	
	@ManyToOne
	private Lead owner;
	
	@Embedded
	private LeadNote leadNote;
	
	@ManyToOne
	private SocialPerson commenter;

	public Long getId() {
		return id;
	}
	
	public SocialPerson getCommenter() {
		return commenter;
	}

	public void setCommenter(SocialPerson commenter) {
		this.commenter = commenter;
	}

	public List<LeadComment> getChildComments() {
		return childComments;
	}

	public void setChildComments(List<LeadComment> childComments) {
		this.childComments = childComments;
	}

	public void setId(Long id) {
		this.id = id;
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
