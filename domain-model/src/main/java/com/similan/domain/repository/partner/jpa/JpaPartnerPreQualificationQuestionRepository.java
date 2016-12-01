package com.similan.domain.repository.partner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.partner.PartnerPreQualificationQuestion;

public interface JpaPartnerPreQualificationQuestionRepository 
                  extends JpaRepository<PartnerPreQualificationQuestion , Long> {
}
