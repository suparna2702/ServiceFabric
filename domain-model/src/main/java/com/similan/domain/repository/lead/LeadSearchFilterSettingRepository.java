package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.lead.LeadSearchFilterSetting;
import com.similan.domain.repository.lead.jpa.JpaLeadSearchFilterSettingRepository;

@Repository
public class LeadSearchFilterSettingRepository {
  @Autowired
  private JpaLeadSearchFilterSettingRepository repository;
	
	public List<LeadSearchFilterSetting> findAll() {
    return repository.findAll();
  }
	
	public LeadSearchFilterSetting findOne(Long id) {
    return repository.findOne(id);
  }
	
	public LeadSearchFilterSetting save(LeadSearchFilterSetting setting) {
    return repository.save(setting);
  }
	
	public List<LeadSearchFilterSetting> getByOwner(SocialPerson person) {
    return repository.getByOwner(person);
  }
	
	public LeadSearchFilterSetting create(){
		LeadSearchFilterSetting setting = new LeadSearchFilterSetting();
		return setting;
	}

}
