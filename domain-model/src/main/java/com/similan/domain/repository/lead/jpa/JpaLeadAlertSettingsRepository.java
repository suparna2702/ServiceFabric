package com.similan.domain.repository.lead.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.lead.LeadAlertSettings;

public interface JpaLeadAlertSettingsRepository 
                          extends JpaRepository<LeadAlertSettings, Long> {
	
	@Query("select leadAlertSettings from LeadAlertSettings leadAlertSettings where leadAlertSettings.owner=:owner")
	public LeadAlertSettings getByOwner(@Param("owner")SocialPerson owner);

}
