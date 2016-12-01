package com.similan.domain.repository.lead.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.lead.CrmLeadSettings;

public interface JpaCrmLeadSettingsRepository extends JpaRepository<CrmLeadSettings, Long> {
	
	@Query("select leadCrmSettings from CrmLeadSettings leadCrmSettings where leadCrmSettings.owner=:owner")
	public CrmLeadSettings getByOwner(@Param("owner")SocialOrganization owner);
}
