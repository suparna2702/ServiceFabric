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

@Entity(name="LeadCaptureQuestionResponse")
public class LeadCaptureQuestionResponse {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
	private Long id;
	
	@Column
	private String questionText;
	
	@Column
	private Long questionId;
	
	@Enumerated(EnumType.STRING)
	@Column
	private LeadCaptureQuestionType questionType;
	
	@OneToMany
	private List<LeadCaptureQuestionChoiceResponse> choiceResponses;

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

	public List<LeadCaptureQuestionChoiceResponse> getChoiceResponses() {
		if(choiceResponses == null){
			choiceResponses = new ArrayList<LeadCaptureQuestionChoiceResponse>();
		}
		return choiceResponses;
	}

	public void setChoiceResponses(
			List<LeadCaptureQuestionChoiceResponse> choiceResponses) {
		this.choiceResponses = choiceResponses;
	}
	
	

}
