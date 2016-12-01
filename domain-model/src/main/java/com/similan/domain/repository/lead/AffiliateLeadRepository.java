package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.AffiliateLead;
import com.similan.domain.entity.lead.LeadSearchFilterSetting;
import com.similan.domain.repository.lead.jpa.JpaAffiliateLeadRepository;

@Repository
public class AffiliateLeadRepository {
  @Autowired
  private JpaAffiliateLeadRepository repository;
	
	public AffiliateLead findOne(Long id) {
    return repository.findOne(id);
  }
	
	public AffiliateLead save(AffiliateLead afflead) {
    return repository.save(afflead);
  }
	
	public List<AffiliateLead> findAll() {
    return repository.findAll();
  }
	
	public List<AffiliateLead> findAllByOwner(Long ownerId) {
    return repository.findAllByOwner(ownerId);
  }
	
	public  Page<AffiliateLead> searchAffiliateLeads(
			   LeadSearchFilterSetting searchSetting, Pageable pagable) {
	  throw new UnsupportedOperationException();
	}
	
	public AffiliateLead create() {
		AffiliateLead affLead = new AffiliateLead();
		return affLead;
	}

}
