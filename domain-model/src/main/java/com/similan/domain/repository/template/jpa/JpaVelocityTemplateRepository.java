package com.similan.domain.repository.template.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.similan.domain.entity.template.VelocityDocumentTemplate;

public interface JpaVelocityTemplateRepository extends
		JpaRepository<VelocityDocumentTemplate, Long> {

	VelocityDocumentTemplate findByName(String name);
	
	@Modifying
	@Query("delete from VelocityDocumentTemplate")
	void deleteEmailTemplates();
	
	@Modifying
	void deleteAllInBatch();

}
