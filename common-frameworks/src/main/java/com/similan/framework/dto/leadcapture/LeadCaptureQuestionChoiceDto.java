package com.similan.framework.dto.leadcapture;

import java.io.Serializable;

public class LeadCaptureQuestionChoiceDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String questionChoiceText;
	
	private int serialId;
	
	public int getSerialId() {
		return serialId;
	}

	public void setSerialId(int serialId) {
		this.serialId = serialId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestionChoiceText() {
		return questionChoiceText;
	}

	public void setQuestionChoiceText(String questionChoiceText) {
		this.questionChoiceText = questionChoiceText;
	}

	@Override
	public String toString() {
		return "LeadCaptureQuestionChoiceDto [id=" + id
				+ ", questionChoiceText=" + questionChoiceText + ", serialId="
				+ serialId + "]";
	}
	
	

}
