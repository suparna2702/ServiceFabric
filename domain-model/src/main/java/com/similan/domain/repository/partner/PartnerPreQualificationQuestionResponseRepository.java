package com.similan.domain.repository.partner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.partner.PartnerPreQualificationQuestionResponse;
import com.similan.domain.repository.partner.jpa.JpaPartnerPreQualificationQuestionResponseRepository;

@Repository
public class PartnerPreQualificationQuestionResponseRepository {
  @Autowired
  private JpaPartnerPreQualificationQuestionResponseRepository repository;
	
	public PartnerPreQualificationQuestionResponse save(PartnerPreQualificationQuestionResponse response) {
    return repository.save(response);
  }
	public PartnerPreQualificationQuestionResponse findOne(Long id) {
    return repository.findOne(id);
  }
	
	public PartnerPreQualificationQuestionResponse create(){
		PartnerPreQualificationQuestionResponse response = new PartnerPreQualificationQuestionResponse();
		return response;
	}

}
