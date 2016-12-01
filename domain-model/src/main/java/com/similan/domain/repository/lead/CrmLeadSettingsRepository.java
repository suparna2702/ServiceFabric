package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.lead.CrmLeadSettings;
import com.similan.domain.repository.lead.jpa.JpaCrmLeadSettingsRepository;

@Repository
public class CrmLeadSettingsRepository {
  @Autowired
  private JpaCrmLeadSettingsRepository repository;
	
	public CrmLeadSettings save(CrmLeadSettings crmLeadSetting) {
    return repository.save(crmLeadSetting);
  }
	
	public CrmLeadSettings findOne(Long id) {
    return repository.findOne(id);
  }
	
	public List<CrmLeadSettings> findAll() {
    return repository.findAll();
  }
	
	public CrmLeadSettings getByOwner(SocialOrganization owner) {
    return repository.getByOwner(owner);
  }
	
	public CrmLeadSettings create(){
		CrmLeadSettings crmLeadSetting = new CrmLeadSettings();
		return crmLeadSetting;
	}

}
