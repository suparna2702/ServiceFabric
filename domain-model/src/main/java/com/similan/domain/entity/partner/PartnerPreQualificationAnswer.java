package com.similan.domain.entity.partner;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "PartnerPreQualAnswer")
public class PartnerPreQualificationAnswer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private Long questionId;
	
	@Column
	private String questionText;
	
	@Enumerated(EnumType.STRING)
	@Column
	private PartnerPreQualificationAnswerType answerType;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<PartnerPreQualificationQuestionResponse> answerChoice;

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

	public PartnerPreQualificationAnswerType getAnswerType() {
		return answerType;
	}

	public void setAnswerType(PartnerPreQualificationAnswerType answerType) {
		this.answerType = answerType;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public List<PartnerPreQualificationQuestionResponse> getAnswerChoice() {
		if(answerChoice == null){
			answerChoice = new ArrayList<PartnerPreQualificationQuestionResponse>();
		}
		return answerChoice;
	}

	public void setAnswerChoice(List<PartnerPreQualificationQuestionResponse> answerChoice) {
		this.answerChoice = answerChoice;
	}
	
}
