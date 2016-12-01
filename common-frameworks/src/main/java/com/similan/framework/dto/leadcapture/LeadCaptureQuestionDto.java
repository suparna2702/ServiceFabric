package com.similan.framework.dto.leadcapture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

import com.similan.domain.entity.leadcapture.LeadCaptureQuestionType;


@Slf4j
public class LeadCaptureQuestionDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private Long id;
	
	private String questionText;
	
	private Boolean required;
	
	private LeadCaptureQuestionType questionType = LeadCaptureQuestionType.MultiChoice;
	
	private List<LeadCaptureQuestionChoiceDto> questionChoices;
	
	private List<LeadCaptureQuestionChoiceDto> selectedChoices;
	
	private LeadCaptureQuestionChoiceDto selectedChoice;
	
	private String textAnswerInput;
	
	private int serialId;
	
	private String questionUUID;
	
	public LeadCaptureQuestionDto(){
		id = Long.MIN_VALUE;
		required = true;
		questionType = LeadCaptureQuestionType.MultiChoice;
		questionChoices = new ArrayList<LeadCaptureQuestionChoiceDto>();
		selectedChoices = new ArrayList<LeadCaptureQuestionChoiceDto>();
		questionUUID = UUID.randomUUID().toString();
		textAnswerInput = StringUtils.EMPTY;
	}
	
	public String getQuestionUUID() {
		return questionUUID;
	}

	public void setQuestionUUID(String questionUUID) {
		this.questionUUID = questionUUID;
	}
	
	public String getTextAnswerInput() {
		return textAnswerInput;
	}

	public void setTextAnswerInput(String textAnswerInput) {
		this.textAnswerInput = textAnswerInput;
	}

	public LeadCaptureQuestionChoiceDto getSelectedChoice() {
		return selectedChoice;
	}

	public void setSelectedChoice(LeadCaptureQuestionChoiceDto selectedChoice) {
		this.selectedChoice = selectedChoice;
	}

	public List<LeadCaptureQuestionChoiceDto> getSelectedChoices() {
		return selectedChoices;
	}

	public void setSelectedChoices(
			List<LeadCaptureQuestionChoiceDto> selectedChoices) {
		this.selectedChoices = selectedChoices;
	}

	public List<String> getSelectedChoiceValues() {
		List<String> values = new ArrayList<String>();
		if (this.selectedChoices == null)
			return values;
		for (LeadCaptureQuestionChoiceDto choice : this.selectedChoices) {
			values.add(choice.getId().toString());
		}
		return values;
	}
	
	public void setSelectedChoiceValues(List<String> values) {
		this.selectedChoices = new ArrayList<LeadCaptureQuestionChoiceDto>();
		for(String value :values ) {
			for(LeadCaptureQuestionChoiceDto choice : this.questionChoices) {
				if(value.equals(choice.getId().toString())) {
					selectedChoices.add(choice);
					break;
				}
			}
		}
	}
	
	public String getSelectedChoiceValue() {
		if (this.selectedChoice == null)
			return null;
		return this.selectedChoice.getId().toString();
	}

	public void setSelectedChoiceValue(String value) {
		for (LeadCaptureQuestionChoiceDto choice : this.questionChoices) {
			if (value.equals(choice.getId().toString())) {
				selectedChoice = choice;
				break;
			}
		}
	}
	
	public boolean getMultiChoice(){
		
		if(questionType.equals(LeadCaptureQuestionType.MultiChoice) == true){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setMultiChoice(boolean choice){
		
	}
	
    public boolean getSingleChoice(){
		
		if(questionType.equals(LeadCaptureQuestionType.SingleChoice) == true){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setSingleChoice(boolean choice){
		
	}
	
    public boolean getTextInput(){
		
		if(questionType.equals(LeadCaptureQuestionType.TextInput) == true){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setTextInput(boolean choice){
		
	}
	
	
	public int getSerialId() {
		return serialId;
	}

	public void setSerialId(int serialId) {
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
		log.info("Lead type " + questionType);
		this.questionType = questionType;
	}

	public List<LeadCaptureQuestionChoiceDto> getQuestionChoices() {
		
		if(questionChoices == null){
			questionChoices = new ArrayList<LeadCaptureQuestionChoiceDto>();
		}
		
		return questionChoices;
	}

	public void setQuestionChoices(
			List<LeadCaptureQuestionChoiceDto> questionChoices) {
		this.questionChoices = questionChoices;
	}

	@Override
	public String toString() {
		return "LeadCaptureQuestionDto [id=" + id + ", questionText="
				+ questionText + ", required=" + required + ", questionType="
				+ questionType + ", questionChoices=" + questionChoices
				+ ", serialId=" + serialId + "]";
	}
	
}
