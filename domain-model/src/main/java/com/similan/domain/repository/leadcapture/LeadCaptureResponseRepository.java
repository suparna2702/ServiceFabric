package com.similan.domain.repository.leadcapture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.leadcapture.LeadCaptureResponse;
import com.similan.domain.repository.leadcapture.jpa.JpaLeadCaptureResponseRepository;

@Repository
public class LeadCaptureResponseRepository {
  @Autowired
  private JpaLeadCaptureResponseRepository repository;
	
	public LeadCaptureResponse save(LeadCaptureResponse response) {
    return repository.save(response);
  }
	
	public LeadCaptureResponse create(){
		LeadCaptureResponse response = new LeadCaptureResponse();
		return response;
	}

}
