package com.similan.framework.dto.partner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.similan.domain.entity.partner.PartnerPreQualificationAnswerType;

public class PartnerPreQualificationAnswerDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private Long id;
	
	private Long questionId;
	
	private String questionText;
	
	private PartnerPreQualificationAnswerType answerType;
	
	private List<PartnerPreQualificationQuestionResponseDto> answerChoices;
	
	public PartnerPreQualificationAnswerDto(){
		id = Long.MIN_VALUE;
		answerType = PartnerPreQualificationAnswerType.MultiChoice;
		answerChoices = new ArrayList<PartnerPreQualificationQuestionResponseDto>();
	}
	
	public boolean getChoice(){
		if(answerType.equals(PartnerPreQualificationAnswerType.MultiChoice) 
				|| answerType.equals(PartnerPreQualificationAnswerType.SingleChoiceList) 
				|| answerType.equals(PartnerPreQualificationAnswerType.SingleChoiceRadio)){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setChoice(boolean choice){
		
	}
	
	public boolean getTextInput(){
		if(answerType.equals(PartnerPreQualificationAnswerType.TextInput)){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setTextInput(){
		
	}
	
	public boolean getRating(){
		if(answerType.equals(PartnerPreQualificationAnswerType.Rating)){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setRating(boolean rating){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<PartnerPreQualificationQuestionResponseDto> getAnswerChoices() {
		return answerChoices;
	}

	public void setAnswerChoices(List<PartnerPreQualificationQuestionResponseDto> answerChoices) {
		this.answerChoices = answerChoices;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
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

}
