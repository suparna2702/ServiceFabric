package com.similan.domain.entity.poll;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.similan.domain.entity.community.SocialActor;

@Entity(name = "PollSubmission")
public class PollSubmission {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private Long pollTemplateId;
	
	@ManyToOne
	private PollEvent pollEventFor;
	
	@Column
	private Date timeStamp;
	
	@ManyToOne
	private SocialActor submitter;
	
	@Enumerated(EnumType.STRING)
	@Column
	private PollSubmissionType submitterType;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<PollAnswer> pollAnswers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getPollTemplateId() {
		return pollTemplateId;
	}

	public void setPollTemplateId(Long pollTemplateId) {
		this.pollTemplateId = pollTemplateId;
	}

	public PollEvent getPollEventFor() {
		return pollEventFor;
	}

	public void setPollEventFor(PollEvent pollEventFor) {
		this.pollEventFor = pollEventFor;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public SocialActor getSubmitter() {
		return submitter;
	}

	public void setSubmitter(SocialActor submitter) {
		this.submitter = submitter;
	}

	public PollSubmissionType getSubmitterType() {
		return submitterType;
	}

	public void setSubmitterType(PollSubmissionType submitterType) {
		this.submitterType = submitterType;
	}

	public List<PollAnswer> getPollAnswers() {
		if(pollAnswers == null){
			pollAnswers = new ArrayList<PollAnswer>();
		}
		return pollAnswers;
	}

	public void setPollAnswers(List<PollAnswer> pollAnswers) {
		this.pollAnswers = pollAnswers;
	}
	
}
