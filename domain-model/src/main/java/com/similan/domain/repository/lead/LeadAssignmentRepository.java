package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.LeadAssignment;
import com.similan.domain.repository.lead.jpa.JpaLeadAssignmentRepository;

@Repository
public class LeadAssignmentRepository {
  @Autowired
  private JpaLeadAssignmentRepository repository;
	
	public LeadAssignment save(LeadAssignment leadAssignment) {
    return repository.save(leadAssignment);
  }
	public List<LeadAssignment> getAssignmentsById(Long id) {
    return repository.getAssignmentsById(id);
  }
	
	public LeadAssignment create(){
		LeadAssignment leadAssignment = new LeadAssignment();
		return leadAssignment;
	}

}
