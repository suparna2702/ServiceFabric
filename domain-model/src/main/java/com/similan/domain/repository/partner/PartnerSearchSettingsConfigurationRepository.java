package com.similan.domain.repository.partner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.partner.PartnerSearchSettingsConfiguration;
import com.similan.domain.repository.partner.jpa.JpaPartnerSearchSettingsConfigurationRepository;

@Repository
public class PartnerSearchSettingsConfigurationRepository {
  @Autowired
  private JpaPartnerSearchSettingsConfigurationRepository repository;
	
	public PartnerSearchSettingsConfiguration save(PartnerSearchSettingsConfiguration config) {
    return repository.save(config);
  }
	
	public PartnerSearchSettingsConfiguration findOne(Long id) {
    return repository.findOne(id);
  }
	
	public PartnerSearchSettingsConfiguration findByOrgId(Long orgId) {
    return repository.findByOrgId(orgId);
  }
	
	public PartnerSearchSettingsConfiguration create(){
		PartnerSearchSettingsConfiguration config = new PartnerSearchSettingsConfiguration();
		return config;
	}
}
