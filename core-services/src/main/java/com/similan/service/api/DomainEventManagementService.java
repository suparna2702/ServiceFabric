package com.similan.service.api;

import java.util.List;

import com.similan.framework.dto.update.SystemUpdateEventDto;
import com.similan.framework.dto.update.WallEventDto;

public interface DomainEventManagementService {
	
	public void deleteSystemUpdate(SystemUpdateEventDto update);
	
	public List<SystemUpdateEventDto> getAllSystemEventsByIndustry(String industry);
	
	public List<SystemUpdateEventDto> getAllSystemEvents();
	
	public void saveSystemUpdate(SystemUpdateEventDto update);
	
	/**
	 * @param industry
	 * @return
	 */
	public long getWallEventsForIndustryAndActorCount(String industry, long forSocialActorId);
	
	/**
	 * @param industry
	 * @param forSocialActorId
	 * @param first
	 * @param pageSize
	 * @return
	 */
	public List<WallEventDto> getWallEventsForIndustryAndActor(String industry, long forSocialActorId, int first, int pageSize);

	/**
	 * @param industry
	 * @return
	 */
	public long getPublicWallEventsForIndustryAndActorCount(long forSocialActorId);
	
	/**
	 * @param industry
	 * @param forSocialActorId
	 * @param first
	 * @param pageSize
	 * @return
	 */
	public List<WallEventDto> getPublicWallEventsForIndustryAndActor(long forSocialActorId, int first, int pageSize);
	
}
