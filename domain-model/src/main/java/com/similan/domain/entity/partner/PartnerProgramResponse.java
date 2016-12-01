package com.similan.domain.entity.partner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name="PartnerProgramResponse")
public class PartnerProgramResponse {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToMany
	private List<PartnerPreQualificationAnswer> partnerPreQualificationAnswer 
	                               = new ArrayList<PartnerPreQualificationAnswer>();
	
	@Column
	private Date timeStamp;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public List<PartnerPreQualificationAnswer> getPartnerPreQualificationAnswer() {
		return partnerPreQualificationAnswer;
	}

	public void setPartnerPreQualificationAnswer(
			List<PartnerPreQualificationAnswer> preQualQuestionResponses) {
		this.partnerPreQualificationAnswer = preQualQuestionResponses;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

}
