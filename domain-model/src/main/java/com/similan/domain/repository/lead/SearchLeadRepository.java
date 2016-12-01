package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.SearchLead;
import com.similan.domain.repository.lead.jpa.JpaSearchLeadRepository;

@Repository
public class SearchLeadRepository {
  @Autowired
  private JpaSearchLeadRepository repository;
	
    public SearchLead findOne(Long id) {
    return repository.findOne(id);
  }
	
	public List<SearchLead> findAll() {
    return repository.findAll();
  }
	
	public List<SearchLead> findLeadsForSocialActor(Long actorId) {
    return repository.findLeadsForSocialActor(actorId);
  }
	
	public SearchLead save(SearchLead searchLead) {
    return repository.save(searchLead);
  }
	
	public Long getLeadCountSocialActor(Long actorId) {
    return repository.getLeadCountSocialActor(actorId);
  }
	
	public List<SearchLead> save(Iterable<SearchLead> searchLead) {
    return repository.save(searchLead);
  }
	
	public void delete(Long id) {
    repository.delete(id);
  }
	
	public SearchLead create(){
		SearchLead searchLead = 
				new SearchLead();
		return searchLead;
	}
	
	public  Long getNotViewedLeadCountSocialActor(Long id) {
    return repository.getNotViewedLeadCountSocialActor(id);
  }

}
