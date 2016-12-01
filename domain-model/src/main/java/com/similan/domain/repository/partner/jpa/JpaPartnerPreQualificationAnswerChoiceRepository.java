package com.similan.domain.repository.partner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.partner.PartnerPreQualificationAnswerChoice;

public interface JpaPartnerPreQualificationAnswerChoiceRepository 
               extends JpaRepository<PartnerPreQualificationAnswerChoice, Long> {

}
