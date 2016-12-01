package com.similan.framework.dto.leadcapture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LeadCaptureWizzardDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String templateName;
	
	private String leadName;
	
	private String leadEmailAddress;
	
	private String additionalComments;
	
	private String company;
	
	private String phoneNumber;
	
	private List<LeadCaptureQuestionDto> questions;
	
	public LeadCaptureWizzardDto(){
		id = Long.MIN_VALUE;
		questions = new ArrayList<LeadCaptureQuestionDto>();
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
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

	public List<LeadCaptureQuestionDto> getQuestions() {
		if(questions == null){
			questions = new ArrayList<LeadCaptureQuestionDto>();	
		}
		
		return questions;
	}

	public void setQuestions(List<LeadCaptureQuestionDto> questions) {
		this.questions = questions;
	}

	@Override
	public String toString() {
		return "LeadCaptureWizzardDto [id=" + id + ", templateName="
				+ templateName + ", questions=" + questions + "]";
	}
	

}
