package com.similan.domain.repository.leadcapture.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.leadcapture.LeadCaptureResponse;

public interface JpaLeadCaptureResponseRepository 
                     extends JpaRepository<LeadCaptureResponse, Long> {

}
