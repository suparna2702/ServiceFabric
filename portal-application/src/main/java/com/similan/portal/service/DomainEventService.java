package com.similan.portal.service;

import java.util.List;

import com.similan.framework.dto.update.SystemUpdateEventDto;
import com.similan.framework.dto.update.WallEventDto;

public interface DomainEventService {

	public List<SystemUpdateEventDto> getAllSystemEventsByIndustry(String industry);

	public List<WallEventDto> getWallEventsForIndustryAndActor(String industry, long forSocialActorId, int first, int pageSize);
	public long getWallEventsForIndustryAndActorCount(String industry, long forSocialActorId);

	public List<WallEventDto> getPublicWallEventsForIndustryAndActor(long forSocialActorId, int first, int pageSize);
	public long getPublicWallEventsForIndustryAndActorCount(long forSocialActorId);

}
