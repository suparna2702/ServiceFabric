package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.lead.LeadAlertSettings;
import com.similan.domain.repository.lead.jpa.JpaLeadAlertSettingsRepository;

@Repository
public class LeadAlertSettingsRepository {
  @Autowired
  private JpaLeadAlertSettingsRepository repository;
	
	public List<LeadAlertSettings> findAll() {
    return repository.findAll();
  }
	
	public LeadAlertSettings findOne(Long id) {
    return repository.findOne(id);
  }
	
	public LeadAlertSettings getByOwner(SocialPerson person) {
    return repository.getByOwner(person);
  }
	
	public LeadAlertSettings save(LeadAlertSettings alertSetting) {
    return repository.save(alertSetting);
  }
	
	public LeadAlertSettings create(){
		LeadAlertSettings alertSettings = new LeadAlertSettings();
		return alertSettings;
	}

}
