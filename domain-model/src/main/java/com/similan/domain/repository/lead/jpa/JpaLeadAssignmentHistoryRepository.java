package com.similan.domain.repository.lead.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.lead.LeadAssignmentHistory;

public interface JpaLeadAssignmentHistoryRepository extends
		JpaRepository<LeadAssignmentHistory, Long> {

}
