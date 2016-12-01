package com.similan.framework.dto.poll;

import java.io.Serializable;

public class PollAnswerChoiceDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private Long id;
	
	private Long choiceId;
	
	private String answerText;
	
	private String choiceText;
	
	private Integer answerRating;

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

	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public String getChoiceText() {
		return choiceText;
	}

	public void setChoiceText(String choiceText) {
		this.choiceText = choiceText;
	}

	public Integer getAnswerRating() {
		return answerRating;
	}

	public void setAnswerRating(Integer answerRating) {
		this.answerRating = answerRating;
	}

}
