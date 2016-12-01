package com.similan.domain.entity.leadcapture;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="LeadCaptureQuestionChoiceResponse")
public class LeadCaptureQuestionChoiceResponse {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
	private Long id;
	
	@Column
	private Long choiceId;
	
	@Column
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
