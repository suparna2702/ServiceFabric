package com.similan.domain.repository.community.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.community.SocialPersonInterest;

public interface JpaSocialPersonInterestRepository 
             extends JpaRepository<SocialPersonInterest, Long> {
	
}
