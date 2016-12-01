package com.similan.domain.repository.partner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.partner.PartnerSearchSettingsConfiguration;

public interface JpaPartnerSearchSettingsConfigurationRepository 
                         extends JpaRepository<PartnerSearchSettingsConfiguration, Long>{
	
	@Query("select partnerSearchSetting from PartnerSearchSettingsConfiguration partnerSearchSetting where partnerSearchSetting.orgId=:orgId")
	public PartnerSearchSettingsConfiguration findByOrgId(@Param("orgId")Long orgId);

}
