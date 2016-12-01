package com.similan.domain.entity.partner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "PartnerPreQualAnswerChoice")
public class PartnerPreQualificationAnswerChoice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private String answerChoiceText;
	
	@Column
	private Integer answerIndex;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAnswerChoiceText() {
		return answerChoiceText;
	}

	public void setAnswerChoiceText(String answerChoiceText) {
		this.answerChoiceText = answerChoiceText;
	}

	public Integer getAnswerIndex() {
		return answerIndex;
	}

	public void setAnswerIndex(Integer answerIndex) {
		this.answerIndex = answerIndex;
	}
}
