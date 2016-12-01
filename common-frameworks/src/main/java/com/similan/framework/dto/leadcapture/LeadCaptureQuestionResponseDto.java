package com.similan.framework.dto.leadcapture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.similan.domain.entity.leadcapture.LeadCaptureQuestionType;

public class LeadCaptureQuestionResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private Long id;
	
	private String questionText;
	
	private Long questionId;
	
	private LeadCaptureQuestionType questionType;
	
	private List<LeadCaptureQuestionChoiceResponseDto> choiceResponses;
	
	public LeadCaptureQuestionResponseDto(){
		id = Long.MIN_VALUE;
		choiceResponses = new ArrayList<LeadCaptureQuestionChoiceResponseDto>();
	}
	
	public List<LeadCaptureQuestionChoiceResponseDto> getChoiceResponses() {
		return choiceResponses;
	}

	public void setChoiceResponses(
			List<LeadCaptureQuestionChoiceResponseDto> choiceResponses) {
		this.choiceResponses = choiceResponses;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public LeadCaptureQuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(LeadCaptureQuestionType questionType) {
		this.questionType = questionType;
	}
	
}
