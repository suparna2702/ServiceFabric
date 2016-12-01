package com.similan.domain.repository.partner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.partner.PartnerProgramCommunicationAttribute;

public interface JpaPartnerProgramCommunicationAttributeRepository extends
		JpaRepository<PartnerProgramCommunicationAttribute, Long> {

}
