package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.AffiliateLeadList;
import com.similan.domain.repository.lead.jpa.JpaAffiliateLeadListRepository;

@Repository
public class AffiliateLeadListRepository {
  @Autowired
  private JpaAffiliateLeadListRepository repository;
	
	public AffiliateLeadList findOne(Long id) {
    return repository.findOne(id);
  }
	
	public List<AffiliateLeadList> findAll() {
    return repository.findAll();
  }
	
	public AffiliateLeadList save(AffiliateLeadList leadList) {
    return repository.save(leadList);
  }
	
	public List<AffiliateLeadList> findByOwner(Long ownerId) {
    return repository.findByOwner(ownerId);
  }
	
	public AffiliateLeadList create(){
		AffiliateLeadList leadList = new AffiliateLeadList();
		return leadList;
	}

}
