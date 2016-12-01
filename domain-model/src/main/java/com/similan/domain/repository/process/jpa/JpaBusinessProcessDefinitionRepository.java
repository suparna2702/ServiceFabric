package com.similan.domain.repository.process.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.process.BusinessProcessDefinition;

public interface JpaBusinessProcessDefinitionRepository extends
		JpaRepository<BusinessProcessDefinition, Long> {

	BusinessProcessDefinition findByName(String name);
}
