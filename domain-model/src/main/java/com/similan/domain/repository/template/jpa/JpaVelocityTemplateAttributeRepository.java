package com.similan.domain.repository.template.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.similan.domain.entity.template.VelocityTemplateAttribute;

public interface JpaVelocityTemplateAttributeRepository extends
		JpaRepository<VelocityTemplateAttribute, Long> {
	@Modifying
	void deleteAllInBatch();

}
