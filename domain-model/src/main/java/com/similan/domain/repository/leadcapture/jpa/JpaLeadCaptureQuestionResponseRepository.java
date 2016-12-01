package com.similan.domain.repository.leadcapture.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.leadcapture.LeadCaptureQuestionResponse;

public interface JpaLeadCaptureQuestionResponseRepository extends
		JpaRepository<LeadCaptureQuestionResponse, Long> {

}
