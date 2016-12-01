package com.similan.domain.entity.leadcapture;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.similan.domain.entity.community.SocialOrganization;

@Entity(name="LeadCaptureResponse")
public class LeadCaptureResponse {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
	private Long id;
	
	@Column
    private String templateName;
	
	@Column
	private String leadName;
	
	@Column
	private String leadEmailAddress;
	
	@Column
	private String company;
	
	@Column
	private String additionalComments;
	
	@Column
	private Date timeStamp;
	
	@ManyToOne
	private SocialOrganization owner;
	
	@OneToMany
	private List<LeadCaptureQuestionResponse> questionResponse;
	
	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public SocialOrganization getOwner() {
		return owner;
	}

	public void setOwner(SocialOrganization owner) {
		this.owner = owner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getLeadName() {
		return leadName;
	}

	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}

	public String getLeadEmailAddress() {
		return leadEmailAddress;
	}

	public void setLeadEmailAddress(String leadEmailAddress) {
		this.leadEmailAddress = leadEmailAddress;
	}

	public String getAdditionalComments() {
		return additionalComments;
	}

	public void setAdditionalComments(String additionalComments) {
		this.additionalComments = additionalComments;
	}

	public List<LeadCaptureQuestionResponse> getQuestionResponse() {
		if(questionResponse == null){
			questionResponse = new ArrayList<LeadCaptureQuestionResponse>();
		}
		return questionResponse;
	}

	public void setQuestionResponse(
			List<LeadCaptureQuestionResponse> questionResponse) {
		this.questionResponse = questionResponse;
	}
	
	

}
