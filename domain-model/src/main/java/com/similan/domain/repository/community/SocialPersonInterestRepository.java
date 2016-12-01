package com.similan.domain.repository.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialPersonInterest;
import com.similan.domain.repository.community.jpa.JpaSocialPersonInterestRepository;

@Repository
public class SocialPersonInterestRepository {
  @Autowired
  private JpaSocialPersonInterestRepository repository;
	
	public SocialPersonInterest save(SocialPersonInterest socialInterest) {
    return repository.save(socialInterest);
  }
	
	public void delete(Long interestId) {
    repository.delete(interestId);
  }
	
	public SocialPersonInterest create(){
		SocialPersonInterest socialInterest = new SocialPersonInterest();
	    return socialInterest;
	}

}
