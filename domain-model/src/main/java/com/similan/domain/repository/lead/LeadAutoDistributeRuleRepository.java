package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.Lead;
import com.similan.domain.entity.lead.LeadAutoDistributeRule;
import com.similan.domain.repository.lead.jpa.JpaLeadAutoDistributeRuleRepository;

@Repository
public class LeadAutoDistributeRuleRepository {
  @Autowired
  private JpaLeadAutoDistributeRuleRepository repository;
	
	public LeadAutoDistributeRule findOne(Long id) {
    return repository.findOne(id);
  }
	
	public LeadAutoDistributeRule save(LeadAutoDistributeRule distributeRule) {
    return repository.save(distributeRule);
  }
	
	public List<LeadAutoDistributeRule> getByOwner(Lead owner) {
    return repository.getByOwner(owner);
  }
	
	public LeadAutoDistributeRule create(){
		LeadAutoDistributeRule distributeRule = new LeadAutoDistributeRule();
		return distributeRule;
	}
	

}
