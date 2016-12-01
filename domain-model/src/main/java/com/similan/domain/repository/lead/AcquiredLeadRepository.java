package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.AcquiredLead;
import com.similan.domain.repository.lead.jpa.JpaAcquiredLeadRepository;

@Repository
public class AcquiredLeadRepository {
  @Autowired
  private JpaAcquiredLeadRepository repository;
	
	public AcquiredLead findOne(Long id) {
    return repository.findOne(id);
  }
	
	public AcquiredLead save(AcquiredLead acqLead) {
    return repository.save(acqLead);
  }
	
	public List<AcquiredLead> findAll() {
    return repository.findAll();
  }
	
	public List<AcquiredLead> findLeadsForSocialActor(Long actorId) {
    return repository.findLeadsForSocialActor(actorId);
  }
	
	public Long getLeadCount() {
    return repository.getLeadCount();
  }
	
	public void delete(Long id) {
    repository.delete(id);
  }
	
	public AcquiredLead create(){
		AcquiredLead acqLead = new AcquiredLead();
		return acqLead;
	}
	
	public Long getNotViewedLeadCountSocialActor(Long id) {
    return repository.getNotViewedLeadCountSocialActor(id);
  }

	public Long getLeadCountSocialActor(long actorId) {
    return repository.getLeadCountSocialActor(actorId);
  }
}
