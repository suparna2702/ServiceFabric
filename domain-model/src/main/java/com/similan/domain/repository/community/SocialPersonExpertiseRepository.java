package com.similan.domain.repository.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialPersonExpertise;
import com.similan.domain.repository.community.jpa.JpaSocialPersonExpertiseRepository;

@Repository
public class SocialPersonExpertiseRepository {
  @Autowired
  private JpaSocialPersonExpertiseRepository repository;
	
	public SocialPersonExpertise save(SocialPersonExpertise expertise) {
    return repository.save(expertise);
  }
	
	public void delete(Long id) {
    repository.delete(id);
  }
	
	public SocialPersonExpertise create(){
		SocialPersonExpertise expertise = new SocialPersonExpertise();
		return expertise;
	}

}
