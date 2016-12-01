package com.similan.domain.repository.leadcapture.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.leadcapture.LeadCaptureQuestion;

public interface JpaLeadCaptureQuestionRepository extends JpaRepository<LeadCaptureQuestion, Long> {
}
