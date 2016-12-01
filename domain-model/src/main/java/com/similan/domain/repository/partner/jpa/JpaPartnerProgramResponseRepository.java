package com.similan.domain.repository.partner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.partner.PartnerProgramResponse;

public interface JpaPartnerProgramResponseRepository extends
		JpaRepository<PartnerProgramResponse, Long> {
	
}
