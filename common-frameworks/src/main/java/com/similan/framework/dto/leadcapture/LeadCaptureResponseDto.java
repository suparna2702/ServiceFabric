package com.similan.framework.dto.leadcapture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LeadCaptureResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
    private String templateName;
	
	private String leadName;
	
	private String leadEmailAddress;
	
	private String company;
	
	private String additionalComments;
	
	private Date timeStamp;
	
	private List<LeadCaptureQuestionResponseDto> questionResponse;
	
	public LeadCaptureResponseDto(){
		id = Long.MIN_VALUE;
		questionResponse = new ArrayList<LeadCaptureQuestionResponseDto>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public List<LeadCaptureQuestionResponseDto> getQuestionResponse() {
		return questionResponse;
	}

	public void setQuestionResponse(
			List<LeadCaptureQuestionResponseDto> questionResponse) {
		this.questionResponse = questionResponse;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAdditionalComments() {
		return additionalComments;
	}

	public void setAdditionalComments(String additionalComments) {
		this.additionalComments = additionalComments;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

}
