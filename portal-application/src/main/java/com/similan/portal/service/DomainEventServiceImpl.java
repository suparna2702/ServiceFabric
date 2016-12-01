package com.similan.portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.framework.dto.update.SystemUpdateEventDto;
import com.similan.framework.dto.update.WallEventDto;
import com.similan.service.api.DomainEventManagementService;

@Service("domainEventService")
public class DomainEventServiceImpl extends BaseService implements DomainEventService {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
    private DomainEventManagementService domainEventService;

	@Transactional
	public List<SystemUpdateEventDto> getAllSystemEventsByIndustry(
			String industry) {
		return domainEventService.getAllSystemEventsByIndustry(industry);
	}

	@Transactional
	public List<WallEventDto> getWallEventsForIndustryAndActor(String industry, long forSocialActorId, int first, int pageSize) {
		return domainEventService.getWallEventsForIndustryAndActor(industry, forSocialActorId, first, pageSize);
	}
	
	@Transactional
	public long getWallEventsForIndustryAndActorCount(String industry, long forSocialActorId) {
		return domainEventService.getWallEventsForIndustryAndActorCount(industry, forSocialActorId);
	}
	
	@Transactional
	public List<WallEventDto> getPublicWallEventsForIndustryAndActor(long forSocialActorId, int first, int pageSize) {
		return domainEventService.getPublicWallEventsForIndustryAndActor(forSocialActorId, first, pageSize);
	}
	
	@Transactional
	public long getPublicWallEventsForIndustryAndActorCount(long forSocialActorId) {
		return domainEventService.getPublicWallEventsForIndustryAndActorCount(forSocialActorId);
	}	
}
