package com.similan.framework.dto.partner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.similan.domain.entity.partner.PartnerPreQualificationAnswerType;


public class PartnerPreQualificationQuestionDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Integer questionIndex;
	
	private String questionText;
	
	private PartnerPreQualificationAnswerType answerType;
	
	private Boolean required;
	
	private List<PartnerPreQualificationAnswerChoiceDto> preQualAnswerChoice;
	
	private List<PartnerPreQualificationAnswerChoiceDto> selectedAnswerChoice;
	
	private PartnerPreQualificationAnswerChoiceDto selectedAnswer;
	
	private String answerText;
	
	private Integer answerRating; 
	
	private String questionUUID;
	
	public PartnerPreQualificationQuestionDto(){
		
		id = Long.MIN_VALUE;
		questionIndex = 0;
		answerType = PartnerPreQualificationAnswerType.MultiChoice;
		questionUUID = UUID.randomUUID().toString();
		preQualAnswerChoice = new ArrayList<PartnerPreQualificationAnswerChoiceDto>();
		selectedAnswerChoice = new ArrayList<PartnerPreQualificationAnswerChoiceDto>();
		questionIndex = 5;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestionUUID() {
		return questionUUID;
	}

	public void setQuestionUUID(String questionUUID) {
		this.questionUUID = questionUUID;
	}

	public boolean getMultiChoice(){
		
		if(answerType.equals(PartnerPreQualificationAnswerType.MultiChoice) == true){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setMultiChoice(boolean choice){
		
	}
	
    public boolean getSingleChoiceRadio(){
		
		if(answerType.equals(PartnerPreQualificationAnswerType.SingleChoiceRadio) == true){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setSingleChoiceRadio(boolean choice){
		
	}
	
    public boolean getSingleChoiceList(){
		
		if(answerType.equals(PartnerPreQualificationAnswerType.SingleChoiceList) == true){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setSingleChoiceList(boolean choice){
		
	}
	
    public boolean getTextInput(){
		
		if(answerType.equals(PartnerPreQualificationAnswerType.TextInput) == true){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setTextInput(boolean choice){
		
	}
	
    public boolean getRating(){
		
		if(answerType.equals(PartnerPreQualificationAnswerType.Rating) == true){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setRating(boolean choice){
		
	}
	
	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
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

	public PartnerPreQualificationAnswerType getAnswerType() {
		return answerType;
	}

	public void setAnswerType(PartnerPreQualificationAnswerType answerType) {
		this.answerType = answerType;
	}
	
	public List<PartnerPreQualificationAnswerChoiceDto> getSelectedAnswerChoice() {
		return selectedAnswerChoice;
	}

	public void setSelectedAnswerChoice(
			List<PartnerPreQualificationAnswerChoiceDto> selectedAnswerChoice) {
		this.selectedAnswerChoice = selectedAnswerChoice;
	}
	
	public PartnerPreQualificationAnswerChoiceDto getSelectedAnswer() {
		return selectedAnswer;
	}

	public void setSelectedAnswer(PartnerPreQualificationAnswerChoiceDto selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
	}
	
	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	
	public Integer getAnswerRating() {
		return answerRating;
	}

	public void setAnswerRating(Integer answerRating) {
		this.answerRating = answerRating;
	}

	public void setSelectedAnswerChoiceIds(Long ids[]) {

		selectedAnswerChoice = new ArrayList<PartnerPreQualificationAnswerChoiceDto>();
		if (ids != null) {
			for (PartnerPreQualificationAnswerChoiceDto answer : preQualAnswerChoice) {
				
				for(int i = 0; i < ids.length; i++){
					Long id = ids[i];
			
				if (id  ==  answer.getId()) {
					selectedAnswerChoice.add(answer);
				}
					
				}


			}
		}
	}
	
	public Long[] getSelectedAnswerChoiceIds() {
		List<Long> result = new ArrayList<Long>();
		if (selectedAnswerChoice != null) {
			for (PartnerPreQualificationAnswerChoiceDto answer : selectedAnswerChoice) {
				result.add(answer.getId());
			}
		}
		Long[] out = new Long[result.size()];
		return result.toArray(out);
	}
	
	public void setSelectedAnswerId(Long id) {
		if (id != null) {
			for (PartnerPreQualificationAnswerChoiceDto answer : preQualAnswerChoice) {
				if (id == answer.getId()) {
					selectedAnswer = answer;
					break;
				}
			}
		} else
		{
			selectedAnswer = null;
		}
	}

	public Long getSelectedAnswerId() {
		if (selectedAnswer != null) {
			return selectedAnswer.getId();

		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return "PartnerPreQualificationQuestionDto [id=" + id
				+ ", questionIndex=" + questionIndex + ", questionText="
				+ questionText + ", answerType=" + answerType + ", required="
				+ required + ", templateAnswerChoice=" + preQualAnswerChoice
				+ ", selectedAnswerChoice=" + selectedAnswerChoice
				+ ", selectedAnswer=" + selectedAnswer + ", answerText="
				+ answerText + ", answerRating=" + answerRating
				+ ", questionUUID=" + questionUUID + "]";
	}

	public List<PartnerPreQualificationAnswerChoiceDto> getPreQualAnswerChoice() {
		return preQualAnswerChoice;
	}

	public void setPreQualAnswerChoice(
			List<PartnerPreQualificationAnswerChoiceDto> preQualAnswerChoice) {
		this.preQualAnswerChoice = preQualAnswerChoice;
	}
}
