package com.similan.domain.repository.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.PartnerSettingsConfiguration;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.repository.community.jpa.JpaPartnerSettingsConfigurationRepository;

@Repository
public class PartnerSettingsConfigurationRepository {
  @Autowired
  private JpaPartnerSettingsConfigurationRepository repository;
	
	public PartnerSettingsConfiguration save(PartnerSettingsConfiguration settings) {
    return repository.save(settings);
  }
	
	public PartnerSettingsConfiguration findPartnerConfigByOrg(SocialOrganization org) {
    return repository.findPartnerConfigByOrg(org);
  }
	
	public PartnerSettingsConfiguration findOne(Long id) {
    return repository.findOne(id);
  }
	
	public PartnerSettingsConfiguration create(SocialOrganization socialOrg){
		
		PartnerSettingsConfiguration programSettings = new PartnerSettingsConfiguration();
		programSettings.setOrganization(socialOrg);
		return programSettings;
		
	}

}
