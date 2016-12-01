package com.similan.domain.repository.leadcapture.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.leadcapture.LeadCaptureQuestionChoice;

public interface JpaLeadCaptureQuestionChoiceRepository extends
		JpaRepository<LeadCaptureQuestionChoice, Long> {

}
