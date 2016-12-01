package com.similan.domain.repository.leadcapture;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.leadcapture.LeadCaptureQuestion;
import com.similan.domain.entity.leadcapture.LeadCaptureQuestionChoice;
import com.similan.domain.entity.leadcapture.LeadCaptureWizzardTemplate;
import com.similan.domain.repository.leadcapture.jpa.JpaLeadCaptureWizzardTemplateRepository;

@Repository
public class LeadCaptureWizzardTemplateRepository {
  @Autowired
  private JpaLeadCaptureWizzardTemplateRepository repository;
	
	public LeadCaptureWizzardTemplate findOne(Long id) {
    return repository.findOne(id);
  }
	
	public List<LeadCaptureWizzardTemplate> findAll() {
    return repository.findAll();
  }
	
	public LeadCaptureWizzardTemplate findByOwner(SocialOrganization owner) {
    return repository.findByOwner(owner);
  }
	
	public LeadCaptureWizzardTemplate save(LeadCaptureWizzardTemplate wizzard) {
    return repository.save(wizzard);
  }
	
	public LeadCaptureWizzardTemplate createWizzardTemplate(){
		LeadCaptureWizzardTemplate wizzardTemplate = new LeadCaptureWizzardTemplate();
		return wizzardTemplate;
	}
	
	public LeadCaptureQuestion createQuestion(){
		LeadCaptureQuestion question = new LeadCaptureQuestion();
		return question;
	}
	
	public LeadCaptureQuestionChoice createQuestionChoice(){
		LeadCaptureQuestionChoice questionChoice = new LeadCaptureQuestionChoice();
		return questionChoice;
	}

}
