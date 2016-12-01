package com.similan.domain.repository.leadcapture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.leadcapture.LeadCaptureQuestionChoiceResponse;
import com.similan.domain.repository.leadcapture.jpa.JpaLeadCaptureQuestionChoiceResponseRepository;

@Repository
public class LeadCaptureQuestionChoiceResponseRepository {
  @Autowired
  private JpaLeadCaptureQuestionChoiceResponseRepository repository;
	
	public LeadCaptureQuestionChoiceResponse save(LeadCaptureQuestionChoiceResponse response) {
    return repository.save(response);
  }
	
	public LeadCaptureQuestionChoiceResponse create(){
		LeadCaptureQuestionChoiceResponse response = new LeadCaptureQuestionChoiceResponse();
		return response;
	}

}
