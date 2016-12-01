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

@Entity(name = "PartnerPreQualQuestion")
public class PartnerPreQualificationQuestion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private Integer questionIndex;
	
	@Column
	private String questionText;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<PartnerPreQualificationAnswerChoice> answerChoice;
	
	@Enumerated(EnumType.STRING)
	@Column
	private PartnerPreQualificationAnswerType answerType;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getQuestionIndex() {
		return questionIndex;
	}

	public void setQuestionIndex(Integer questionIndex) {
		this.questionIndex = questionIndex;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public List<PartnerPreQualificationAnswerChoice> getAnswerChoice() {
		
		if(answerChoice == null){
			answerChoice = new ArrayList<PartnerPreQualificationAnswerChoice>();
		}
		return answerChoice;
	}

	public void setAnswerChoice(
			List<PartnerPreQualificationAnswerChoice> templateAnswerChoice) {
		this.answerChoice = templateAnswerChoice;
	}

	public PartnerPreQualificationAnswerType getAnswerType() {
		return answerType;
	}

	public void setAnswerType(PartnerPreQualificationAnswerType answerType) {
		this.answerType = answerType;
	}
	
}
