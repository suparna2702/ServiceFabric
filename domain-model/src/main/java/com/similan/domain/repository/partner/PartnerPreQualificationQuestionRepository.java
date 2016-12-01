package com.similan.domain.repository.partner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.partner.PartnerPreQualificationQuestion;
import com.similan.domain.repository.partner.jpa.JpaPartnerPreQualificationQuestionRepository;

@Repository
public class PartnerPreQualificationQuestionRepository {
  @Autowired
  private JpaPartnerPreQualificationQuestionRepository repository;

	public PartnerPreQualificationQuestion findOne(Long id) {
    return repository.findOne(id);
  }

	public PartnerPreQualificationQuestion save(
			PartnerPreQualificationQuestion pollTemplate) {
    return repository.save(
			pollTemplate);
  }

	public void delete(Long id) {
    repository.delete(id);
  }

	public PartnerPreQualificationQuestion create() {
		PartnerPreQualificationQuestion partnerPreQualificationQuestion = new PartnerPreQualificationQuestion();
		return partnerPreQualificationQuestion;
	}

}
