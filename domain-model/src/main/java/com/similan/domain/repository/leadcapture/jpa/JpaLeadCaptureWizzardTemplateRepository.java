package com.similan.domain.repository.leadcapture.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.leadcapture.LeadCaptureWizzardTemplate;

public interface JpaLeadCaptureWizzardTemplateRepository extends
		JpaRepository<LeadCaptureWizzardTemplate, Long> {
	
	@Query("select leadWizzard from LeadCaptureWizzardTemplate leadWizzard where leadWizzard.owner=:owner")
	public abstract LeadCaptureWizzardTemplate findByOwner(@Param("owner")SocialOrganization owner);

}
