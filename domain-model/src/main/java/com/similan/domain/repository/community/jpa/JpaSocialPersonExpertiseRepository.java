package com.similan.domain.repository.community.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.community.SocialPersonExpertise;

public interface JpaSocialPersonExpertiseRepository extends
		JpaRepository<SocialPersonExpertise, Long> {
	
}
