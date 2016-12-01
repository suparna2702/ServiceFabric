package com.similan.domain.entity.leadcapture;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="LeadCaptureQuestionChoice")
public class LeadCaptureQuestionChoice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
    private Long id;
	
	@Column(length=2000)
	private String questionChoiceText;
	
	@Column
	private Integer serialId;
	
	public Integer getSerialId() {
		return serialId;
	}

	public void setSerialId(Integer serialId) {
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

}
