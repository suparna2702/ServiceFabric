package com.similan.portal.service;

import java.util.List;

import com.similan.domain.entity.metadata.CountryType;
import com.similan.domain.entity.metadata.IndustryType;
import com.similan.framework.dto.Image;

public interface PlatformMetadataService {
	
	public List<IndustryType> getAllIndustryType();
	
	public List<CountryType> getAllCountry();
	
	public List<Image> getHomePageSlideshowImages();

}
