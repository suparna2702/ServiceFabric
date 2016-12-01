package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.Lead;
import com.similan.domain.entity.lead.LeadComment;
import com.similan.domain.repository.lead.jpa.JpaLeadCommentRepository;

@Repository
public class LeadCommentRepository {
  @Autowired
  private JpaLeadCommentRepository repository;
	
	public LeadComment findOne(Long id) {
    return repository.findOne(id);
  }
	
	public LeadComment save(LeadComment leadComment) {
    return repository.save(leadComment);
  }
	
	public List<LeadComment> getLeadCommentByOwner(Lead owner) {
    return repository.getLeadCommentByOwner(owner);
  }
	
	public void delete(Long id) {
    repository.delete(id);
  }
	
	public LeadComment create(){
		LeadComment leadComment = new LeadComment();
		return leadComment;
	}

}
