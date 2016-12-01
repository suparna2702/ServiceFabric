package com.similan.adminapp.service;

import java.util.List;

import com.similan.framework.dto.metadata.IndustryTypeDto;
import com.similan.framework.dto.update.SystemUpdateEventDto;

public interface MetaDataAdminService {
	public List<IndustryTypeDto> getIndustryList();

	public List<SystemUpdateEventDto> getAllSystemUpdates();
	
	public void deleteSystemUpdate(SystemUpdateEventDto update);

	public void saveSystemUpdate(SystemUpdateEventDto updateItem);

}
