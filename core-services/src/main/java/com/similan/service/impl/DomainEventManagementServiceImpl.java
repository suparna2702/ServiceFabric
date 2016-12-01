package com.similan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.partner.PartnerProgramDefinitionRepository;
import com.similan.domain.repository.partner.PartnershipRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.dto.update.SystemUpdateEventDto;
import com.similan.framework.dto.update.WallEventDto;
import com.similan.framework.template.TemplateResourceService;
import com.similan.service.api.DomainEventManagementService;

public class DomainEventManagementServiceImpl implements
		DomainEventManagementService {
	@Autowired
	private PartnershipRepository partnershipRepository;
	@Autowired
	private PartnerProgramDefinitionRepository partnerProgramDefinitionRepository;
	@Autowired
	private SocialOrganizationRepository socialOrganizationRepository;
	@Autowired
	private SocialPersonRepository socialPersonRepository;
	@Autowired
	private TemplateResourceService templateResourceService;
	@Autowired
	private PlatformCommonSettings platformCommonSettings;

	
	public void deleteSystemUpdate(SystemUpdateEventDto update){
	  throw new UnsupportedOperationException();
	}
	
	public void saveSystemUpdate(SystemUpdateEventDto update){
    throw new UnsupportedOperationException();
	}

   public List<SystemUpdateEventDto> getAllSystemEventsByIndustry(String industry){
     throw new UnsupportedOperationException();
   }
   
	public List<SystemUpdateEventDto> getAllSystemEvents() {
    throw new UnsupportedOperationException();
	}
	
	public long getWallEventsForIndustryAndActorCount(String industry,
			long forSocialActorId) {
    throw new UnsupportedOperationException();
	}

	public List<WallEventDto> getWallEventsForIndustryAndActor(
			String industry, long forSocialActorId, int first, int pageSize) {
    throw new UnsupportedOperationException();
	}
	
	public long getPublicWallEventsForIndustryAndActorCount(
			long fromSocialActorId) {
    throw new UnsupportedOperationException();
	}

	public List<WallEventDto> getPublicWallEventsForIndustryAndActor(
			long fromSocialActorId, int first, int pageSize) {
    throw new UnsupportedOperationException();
	}
}
