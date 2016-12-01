package com.similan.domain.repository.partner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.partner.PartnerPreQualificationAnswer;
import com.similan.domain.repository.partner.jpa.JpaPartnerPreQualificationAnswerRepository;

@Repository
public class PartnerPreQualificationAnswerRepository {
  @Autowired
  private JpaPartnerPreQualificationAnswerRepository repository;
	
	public PartnerPreQualificationAnswer findOne(Long id) {
    return repository.findOne(id);
  }
	public PartnerPreQualificationAnswer save(PartnerPreQualificationAnswer answer) {
    return repository.save(answer);
  }
	
	public PartnerPreQualificationAnswer create(){
		PartnerPreQualificationAnswer answer = new PartnerPreQualificationAnswer();
		return answer;
	}
	

}
