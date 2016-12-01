package com.similan.framework.dto.partner;

import java.io.Serializable;

public class PartnerPreQualificationAnswerChoiceDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String answerChoiceText;
	
	private Integer answerRating;
	
	private int answerIndex;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getAnswerRating() {
		return answerRating;
	}

	public void setAnswerRating(Integer answerRating) {
		this.answerRating = answerRating;
	}

	public String getAnswerChoiceText() {
		return answerChoiceText;
	}

	public void setAnswerChoiceText(String answerChoiceText) {
		this.answerChoiceText = answerChoiceText;
	}

	public int getAnswerIndex() {
		return answerIndex;
	}

	public void setAnswerIndex(int answerIndex) {
		this.answerIndex = answerIndex;
	}

}
