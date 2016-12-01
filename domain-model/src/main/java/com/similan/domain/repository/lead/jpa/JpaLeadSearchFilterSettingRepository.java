package com.similan.domain.repository.lead.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.lead.LeadSearchFilterSetting;

public interface JpaLeadSearchFilterSettingRepository extends
		JpaRepository<LeadSearchFilterSetting, Long> {
	
	@Query("select leadSearchFilterSettings from LeadSearchFilterSetting leadSearchFilterSettings where leadSearchFilterSettings.owner=:owner")
	public List<LeadSearchFilterSetting> getByOwner(@Param("owner")SocialPerson person);

}
