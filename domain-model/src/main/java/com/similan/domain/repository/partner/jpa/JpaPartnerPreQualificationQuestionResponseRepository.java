package com.similan.domain.repository.partner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.partner.PartnerPreQualificationQuestionResponse;

public interface JpaPartnerPreQualificationQuestionResponseRepository extends
		JpaRepository<PartnerPreQualificationQuestionResponse, Long> {

}
