package com.similan.framework.dto.leadcapture;

import java.io.Serializable;

public class LeadCaptureQuestionChoiceResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private Long id;
	
	private Long choiceId;
	
	private String questionChoiceText;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(Long choiceId) {
		this.choiceId = choiceId;
	}

	public String getQuestionChoiceText() {
		return questionChoiceText;
	}

	public void setQuestionChoiceText(String questionChoiceText) {
		this.questionChoiceText = questionChoiceText;
	}
	
	

}
