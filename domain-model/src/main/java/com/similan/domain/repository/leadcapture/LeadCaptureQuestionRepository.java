package com.similan.domain.repository.leadcapture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.leadcapture.LeadCaptureQuestion;
import com.similan.domain.repository.leadcapture.jpa.JpaLeadCaptureQuestionRepository;

@Repository
public class LeadCaptureQuestionRepository {
  @Autowired
  private JpaLeadCaptureQuestionRepository repository;
	
	public LeadCaptureQuestion save(LeadCaptureQuestion question) {
    return repository.save(question);
  }
	public void delete(Long id) {
    repository.delete(id);
  }
	
	public LeadCaptureQuestion create(){
		LeadCaptureQuestion question = new LeadCaptureQuestion();
		return question;
	}

}
