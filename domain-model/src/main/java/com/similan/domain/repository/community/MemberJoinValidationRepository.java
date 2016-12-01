package com.similan.domain.repository.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.MemberJoinValidation;
import com.similan.domain.repository.community.jpa.JpaMemberJoinValidationRepository;

/**
 * 
 * @author supapal
 * 
 */
@Repository
public class MemberJoinValidationRepository {
  @Autowired
  private JpaMemberJoinValidationRepository repository;

	public MemberJoinValidation findByMemberId(long id) {
    return repository.findByMemberId(id);
  }
	
	public MemberJoinValidation save(MemberJoinValidation entity) {
    return repository.save(entity);
  }

	public MemberJoinValidation create() {
		return new MemberJoinValidation();
	}


}
