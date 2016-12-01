package com.similan.domain.repository.leadcapture.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.leadcapture.LeadCaptureQuestionChoiceResponse;

public interface JpaLeadCaptureQuestionChoiceResponseRepository extends
		JpaRepository<LeadCaptureQuestionChoiceResponse, Long> {

}
