package com.similan.domain.entity.poll;

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

@Entity(name = "PollTemplateQuestion")
public class PollTemplateQuestion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private Integer questionIndex;
	
	@Column(length=5000)
	private String questionText;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<PollTemplateAnswerChoice> templateAnswerChoice;
	
	@Enumerated(EnumType.STRING)
	@Column
	private PollAnswerType answerType;
	
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

	public List<PollTemplateAnswerChoice> getTemplateAnswerChoice() {
		
		if(templateAnswerChoice == null){
			templateAnswerChoice = new ArrayList<PollTemplateAnswerChoice>();
		}
		return templateAnswerChoice;
	}

	public void setTemplateAnswerChoice(
			List<PollTemplateAnswerChoice> templateAnswerChoice) {
		this.templateAnswerChoice = templateAnswerChoice;
	}

	public PollAnswerType getAnswerType() {
		return answerType;
	}

	public void setAnswerType(PollAnswerType answerType) {
		this.answerType = answerType;
	}
	
}
