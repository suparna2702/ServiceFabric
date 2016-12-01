package com.similan.domain.repository.leadcapture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.leadcapture.LeadCaptureQuestionResponse;
import com.similan.domain.repository.leadcapture.jpa.JpaLeadCaptureQuestionResponseRepository;

@Repository
public class LeadCaptureQuestionResponseRepository {
  @Autowired
  private JpaLeadCaptureQuestionResponseRepository repository;
	
	public LeadCaptureQuestionResponse save(LeadCaptureQuestionResponse response) {
    return repository.save(response);
  }
	
	public LeadCaptureQuestionResponse create(){
		LeadCaptureQuestionResponse response = new LeadCaptureQuestionResponse();
		return response;
	}

}
