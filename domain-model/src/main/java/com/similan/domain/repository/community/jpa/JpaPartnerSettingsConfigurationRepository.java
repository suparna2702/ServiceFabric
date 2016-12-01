package com.similan.domain.repository.community.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.PartnerSettingsConfiguration;
import com.similan.domain.entity.community.SocialOrganization;

public interface JpaPartnerSettingsConfigurationRepository extends 
                            JpaRepository<PartnerSettingsConfiguration, Long>{
	
	@Query("select partnerSettings from PartnerSettingsConfiguration partnerSettings where partnerSettings.organization=:org")
	public PartnerSettingsConfiguration findPartnerConfigByOrg(@Param("org")SocialOrganization org);

}
