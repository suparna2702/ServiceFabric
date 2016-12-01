package com.similan.portal.databean;

import java.util.Map;

import com.similan.service.api.base.dto.key.EntityType;

public interface BookmarkEnitityUrlMap {
	
	public Map<EntityType, String> getUrlMap();
	
	public String getUrlForEntity(EntityType entityType);

}
