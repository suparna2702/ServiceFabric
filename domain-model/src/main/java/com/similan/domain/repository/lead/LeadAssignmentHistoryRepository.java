package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.LeadAssignmentHistory;
import com.similan.domain.repository.lead.jpa.JpaLeadAssignmentHistoryRepository;

@Repository
public class LeadAssignmentHistoryRepository {
  @Autowired
  private JpaLeadAssignmentHistoryRepository repository;
	
    public LeadAssignmentHistory findOne(Long id) {
    return repository.findOne(id);
  }	
	public List<LeadAssignmentHistory> findAll() {
    return repository.findAll();
  }
	public LeadAssignmentHistory save(LeadAssignmentHistory assignmentHistory) {
    return repository.save(assignmentHistory);
  }
	
	public LeadAssignmentHistory create(){
		LeadAssignmentHistory leadHistory = new LeadAssignmentHistory();
		return leadHistory;
	}

}
