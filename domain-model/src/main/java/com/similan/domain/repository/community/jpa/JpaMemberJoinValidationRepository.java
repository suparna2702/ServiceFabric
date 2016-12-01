package com.similan.domain.repository.community.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.community.MemberJoinValidation;

/**
 * 
 * @author supapal
 * 
 */
public interface JpaMemberJoinValidationRepository extends
		JpaRepository<MemberJoinValidation, Long> {

	MemberJoinValidation findByMemberId(long id);
}
