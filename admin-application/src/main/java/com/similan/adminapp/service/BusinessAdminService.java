package com.similan.adminapp.service;

import java.util.List;

import com.similan.framework.dto.OrganizationBasicInfoDto;

public interface BusinessAdminService {
	
	public List<OrganizationBasicInfoDto> getAllBusiness();
	
	public void upgradeAccount(Long orgId);
	
	public void downgradeAccount(Long orgId);

}
