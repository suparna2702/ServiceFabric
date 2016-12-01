package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.ContactLead;
import com.similan.domain.repository.lead.jpa.JpaContactLeadRepository;

@Repository
public class ContactLeadRepository {
  @Autowired
  private JpaContactLeadRepository repository;
	
	public ContactLead findOne(Long id) {
    return repository.findOne(id);
  }
	
	public List<ContactLead> findAll() {
    return repository.findAll();
  }
	
	public List<ContactLead> findLeadsForSocialActor(Long actorId) {
    return repository.findLeadsForSocialActor(actorId);
  }
	
	public ContactLead save(ContactLead message) {
    return repository.save(message);
  }
	
	public Long getLeadCount() {
    return repository.getLeadCount();
  }
	
	public Long getLeadCountSocialActor(Long actorId) {
    return repository.getLeadCountSocialActor(actorId);
  }
	
	public void delete(Long id) {
    repository.delete(id);
  }
	
	public ContactLead create(){
		ContactLead message = 
				new ContactLead();
		return message;
	}

	public Long getNotViewedLeadCountSocialActor(Long id) {
    return repository.getNotViewedLeadCountSocialActor(id);
  }
}
