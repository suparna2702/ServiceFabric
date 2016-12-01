package com.similan.domain.repository.lead.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.lead.LeadAssignment;

public interface JpaLeadAssignmentRepository 
                       extends JpaRepository<LeadAssignment, Long> {
	
	@Query("select assLead from LeadAssignment assLead where assLead.toAssigned=:assignedTo")
	public List<LeadAssignment> getAssignmentsById(@Param("assignedTo")Long assignedTo);

}
