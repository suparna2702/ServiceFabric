package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.ClickThroughLead;
import com.similan.domain.repository.lead.jpa.JpaClickThroughLeadRepository;

@Repository
public class ClickThroughLeadRepository {
  @Autowired
  private JpaClickThroughLeadRepository repository;
	
    public ClickThroughLead findOne(Long id) {
    return repository.findOne(id);
  }
    
    public List<ClickThroughLead> findAll() {
    return repository.findAll();
  }
    
    public List<ClickThroughLead> findLeadsForSocialActor(Long actorId) {
    return repository.findLeadsForSocialActor(actorId);
  }
    
    public Long getLeadCount() {
    return repository.getLeadCount();
  }
	
	public ClickThroughLead save(ClickThroughLead message) {
    return repository.save(message);
  }
	
	public Long getLeadCountSocialActor(Long actorId) {
    return repository.getLeadCountSocialActor(actorId);
  }
	
	public void delete(Long id) {
    repository.delete(id);
  }
	
	public ClickThroughLead create(){
		ClickThroughLead lead = 
				new ClickThroughLead();
		return lead;
	}

	public Long getNotViewedLeadCountSocialActor(Long id) {
    return repository.getNotViewedLeadCountSocialActor(id);
  }
}
