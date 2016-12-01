package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.Lead;
import com.similan.domain.entity.lead.LeadActivity;
import com.similan.domain.repository.lead.jpa.JpaLeadActivityRepository;

@Repository
public class LeadActivityRepository {
  @Autowired
  private JpaLeadActivityRepository repository;
	
	public LeadActivity findOne(Long id) {
    return repository.findOne(id);
  }
	
	public LeadActivity save(LeadActivity leadActivity) {
    return repository.save(leadActivity);
  }
	
	public List<LeadActivity> getLeadActivityByOwner(Lead owner) {
    return repository.getLeadActivityByOwner(owner);
  }
	
	public List<LeadActivity> getLeadActivityByOrganization(Long orgId) {
    return repository.getLeadActivityByOrganization(orgId);
  }
	
	public void delete(Long id) {
    repository.delete(id);
  }
	
	public LeadActivity create(){
		LeadActivity leadActivity = new LeadActivity();
		return leadActivity;
	}

}
