package com.similan.domain.repository.partner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.partner.PartnerProgramRequiredAttribute;

public interface JpaPartnerProgramRequiredAttributeRepository extends
		JpaRepository<PartnerProgramRequiredAttribute, Long> {
	
}
