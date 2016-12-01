package com.similan.portal.databean;

import java.util.Map;

import com.similan.service.api.base.dto.key.EntityType;

public class BookmarkEnitityUrlMapImpl implements BookmarkEnitityUrlMap {
	
	private Map<EntityType, String> urlMap;
	
	public void setUrlMap(Map<EntityType, String> urlMap) {
		this.urlMap = urlMap;
	}

	@Override
	public Map<EntityType, String> getUrlMap() {
		return urlMap;
	}

	@Override
	public String getUrlForEntity(EntityType entityType) {
		return urlMap.get(entityType);
	}

}
