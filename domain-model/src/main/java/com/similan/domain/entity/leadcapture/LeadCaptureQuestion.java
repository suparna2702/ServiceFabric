package com.similan.domain.entity.leadcapture;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name="LeadCaptureQuestion")
public class LeadCaptureQuestion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
	private Long id;
	
	@Column(length=2000)
	private String questionText;
	
	@Enumerated(EnumType.STRING)
	@Column
	private LeadCaptureQuestionType questionType;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean required;
	
	@OneToMany
	private List<LeadCaptureQuestionChoice> questionChoices;
	
	@Column
	private Integer serialId;
	
	public Integer getSerialId() {
		return serialId;
	}

	public void setSerialId(Integer serialId) {
		this.serialId = serialId;
	}

	
	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
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

	public LeadCaptureQuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(LeadCaptureQuestionType questionType) {
		this.questionType = questionType;
	}

	public List<LeadCaptureQuestionChoice> getQuestionChoices() {
		
		if(questionChoices == null){
			questionChoices = new ArrayList<LeadCaptureQuestionChoice>();
		}
		return questionChoices;
	}

	public void setQuestionChoices(
			List<LeadCaptureQuestionChoice> questionChoices) {
		this.questionChoices = questionChoices;
	}
}
