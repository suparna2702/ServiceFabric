package com.similan.portal.view.leadportal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.leadcapture.LeadCaptureQuestionChoiceDto;
import com.similan.framework.dto.leadcapture.LeadCaptureQuestionDto;
import com.similan.framework.dto.leadcapture.LeadCaptureWizzardDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("leadCaptureWizzardCreateView")
@Slf4j
public class LeadCaptureWizzardCreateView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	
	private LeadCaptureQuestionDto leadCaptureQuestion = new LeadCaptureQuestionDto();
	
	private LeadCaptureWizzardDto captureWizzard;
	
	private String questionChoice;
	
	@Autowired
	private OrganizationDetailInfoDto orgInfo;
	
	private boolean showChoice = true;
	
	@PostConstruct
	public void init(){
		
		log.info("Initialization of LeadCaptureWizzardCreateView ");
		captureWizzard = this.getOrgService().getLeadCaptureWizzard(orgInfo);
		
		if(captureWizzard == null){
			captureWizzard = new LeadCaptureWizzardDto();
		}
		
	}
	
    public void showHideChoice(){
		
		log.info("Show choice value " + this.showChoice 
				+ " question type " + leadCaptureQuestion.getQuestionType());
		
		switch(leadCaptureQuestion.getQuestionType()){
		   case MultiChoice:
		   case SingleChoice:
			        this.showChoice = true;
			        break;
		   case TextInput:
			   this.showChoice = false;
		       break;
		   default: break;
		}
	}
	
	public boolean getShowChoice() {
		return showChoice;
	}

	public void setShowChoice(boolean showChoice) {
		this.showChoice = showChoice;
	}

	public LeadCaptureWizzardDto getCaptureWizzard() {
		return captureWizzard;
	}

	public void setCaptureWizzard(LeadCaptureWizzardDto captureWizzard) {
		this.captureWizzard = captureWizzard;
	}

	public String getQuestionChoice() {
		return questionChoice;
	}

	public void setQuestionChoice(String questionChoice) {
		this.questionChoice = questionChoice;
	}

	public LeadCaptureQuestionDto getLeadCaptureQuestion() {
		return leadCaptureQuestion;
	}

	public void setLeadCaptureQuestion(LeadCaptureQuestionDto leadCaptureQuestion) {
		this.leadCaptureQuestion = leadCaptureQuestion;
	}
	
	public void addQuestionChoice(){
		log.info("Add question choice " + this.questionChoice);
		if (!StringUtils.isBlank(questionChoice.trim())) {

			LeadCaptureQuestionChoiceDto choiceDto = new LeadCaptureQuestionChoiceDto();
			choiceDto.setQuestionChoiceText(questionChoice);

			this.leadCaptureQuestion.getQuestionChoices().add(choiceDto);
			questionChoice = "";
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Unable to add question choice",
							"The question choice cannot be empty"));
		}
	}
	
	public void editQuestionChoice(int serialId){
		log.info("Edit question choice " + serialId );
	}
	
	public void editQuestion(int serialId){
		log.info("Edit question " + serialId );
	}

	public void deleteQuestionChoice(int serialId){
		log.info("deleting question choice " + serialId );
	}
	
	public void addQuestion(){
		log.info("adding question ");
		List<String> errors = validateQuestion(leadCaptureQuestion);
		if (errors.isEmpty()) {
		
		captureWizzard.getQuestions().add(leadCaptureQuestion);
		leadCaptureQuestion = new LeadCaptureQuestionDto();
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"Unable to add question: "));
			for (String error : errors) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "*",
								error));
			}
		}
		
	}
	

	private List<String> validateQuestion(LeadCaptureQuestionDto question) {
		List<String> results = new ArrayList<String>();

		// Question text cannot be empty
		if (StringUtils.isBlank(question.getQuestionText().trim()))
			results.add("The question must be specified");

		// Multi choice questions always should have more than one choice
		// and choice text cannot be empty
		if (question.getMultiChoice()
				&& question.getQuestionChoices().size() < 2)
			results.add("Multiple choice questions must have more then one possible answer.");

		// Single choice (List and Radio) much have more than one choice and
		// choice text cannot be empty
		if ((question.getSingleChoice())
				&& question.getQuestionChoices().size() < 2)
			results.add("Single choice questions must have more then one possible answer.");


		return results;
	}
	
	public void deleteQuestion(String uuId){
		log.info("deleting question " + uuId );
		
		Iterator<LeadCaptureQuestionDto> questionIterator = captureWizzard.getQuestions().iterator();
		while(questionIterator.hasNext()){
			
			LeadCaptureQuestionDto question = questionIterator.next();
			if(question.getQuestionUUID().equalsIgnoreCase(uuId) == true){
				
				questionIterator.remove();				
				if(question.getId() != Long.MIN_VALUE){
					this.getOrgService().deleteLeadCaptureQuestion(orgInfo, question);
				}
			}
		}
	}
	
	public void saveWizzard(){
		try {
			log.info("Save wizzard ");
			this.getOrgService().saveLeadCaptureWizzard(captureWizzard, orgInfo);
			FacesContext.getCurrentInstance().addMessage(null, 
	                   new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", 
	                   "Successfully saved lead capture wizzard "));
		}
		catch(Exception exp){
           log.error("Error saving lead capture wizzard ", exp);
           FacesContext.getCurrentInstance().addMessage(null, 
                   new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
                   "Error saving lead capture wizzard " + exp));
		}
	}

}
