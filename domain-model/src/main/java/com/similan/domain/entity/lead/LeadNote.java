package com.similan.domain.entity.lead;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

public class LeadNote implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(length=700)
	private String subject;
	
	@Column(length=7000)
	private String message;
	
	private Date timeStamp;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	

}
