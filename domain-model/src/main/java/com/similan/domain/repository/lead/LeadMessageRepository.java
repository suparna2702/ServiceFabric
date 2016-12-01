package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.Lead;
import com.similan.domain.entity.lead.LeadMessage;
import com.similan.domain.repository.lead.jpa.JpaLeadMessageRepository;

@Repository
public class LeadMessageRepository {
  @Autowired
  private JpaLeadMessageRepository repository;
	
	public LeadMessage findOne(Long id) {
    return repository.findOne(id);
  }
	
	public LeadMessage save(LeadMessage leadMessage) {
    return repository.save(leadMessage);
  }
	
	public List<LeadMessage> getLeadCommentByOwner(Lead owner) {
    return repository.getLeadCommentByOwner(owner);
  }
	
	public void delete(Long id) {
    repository.delete(id);
  }
	
	public LeadMessage create(){
		LeadMessage leadMessage = new LeadMessage();
		return leadMessage;
	}
}
