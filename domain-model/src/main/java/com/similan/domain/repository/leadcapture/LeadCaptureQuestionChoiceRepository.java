package com.similan.domain.repository.leadcapture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.leadcapture.LeadCaptureQuestionChoice;
import com.similan.domain.repository.leadcapture.jpa.JpaLeadCaptureQuestionChoiceRepository;

@Repository
public class LeadCaptureQuestionChoiceRepository {
  @Autowired
  private JpaLeadCaptureQuestionChoiceRepository repository;
	
	public LeadCaptureQuestionChoice save(LeadCaptureQuestionChoice qChoice) {
    return repository.save(qChoice);
  }
	
	public LeadCaptureQuestionChoice create(){
		LeadCaptureQuestionChoice qChoice = new LeadCaptureQuestionChoice();
		return qChoice;
	}

}
