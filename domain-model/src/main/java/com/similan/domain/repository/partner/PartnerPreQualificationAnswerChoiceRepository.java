package com.similan.domain.repository.partner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.partner.PartnerPreQualificationAnswerChoice;
import com.similan.domain.repository.partner.jpa.JpaPartnerPreQualificationAnswerChoiceRepository;

@Repository
public class PartnerPreQualificationAnswerChoiceRepository {
  @Autowired
  private JpaPartnerPreQualificationAnswerChoiceRepository repository;

	public PartnerPreQualificationAnswerChoice findOne(Long id) {
    return repository.findOne(id);
  }

	public PartnerPreQualificationAnswerChoice save(
			PartnerPreQualificationAnswerChoice pollTemplateQuestionChoice) {
    return repository.save(
			pollTemplateQuestionChoice);
  }

	public PartnerPreQualificationAnswerChoice create() {
		PartnerPreQualificationAnswerChoice partnerPreQualificationAnswerChoice = new PartnerPreQualificationAnswerChoice();
		return partnerPreQualificationAnswerChoice;
	}

}
