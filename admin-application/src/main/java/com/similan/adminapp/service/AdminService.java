package com.similan.adminapp.service;

import java.util.List;
import java.util.Map;

import com.similan.framework.dto.admin.AdminAccountDto;

public interface AdminService {
	
	public Map<String, Long> getLeadImportCountOverTimeperiod(int startPeriod,
			int frequency);
	
	public Map<String, Long> getCrmSyncCountOverTimeperiod(int startPeriod,
			int frequency);
	
	public Map<String, Long> getLeadTransferCountOverTimeperiod(
			int startPeriod, int frequency);
	public List<AdminAccountDto> getAllAdminAccount();
	
	public Map<String, Long> getPollSubmissionCountOverTimeperiod(int startPeriod,
			int frequency);
	
	public Map<String, Long> getPollEventCountOverTimeperiod(int startPeriod,
			int frequency);
	
	public Map<String, Long> getPartnerSearchEventCountOverTimeperiod(
			int startPeriod, int frequency);
	
	public Map<String, Long> getCommunitySearchEventCountOverTimeperiod(
			int startPeriod, int frequency);
	
	public Map<String, Long> getWallEventCountOverTimeperiod(int startPeriod,
			int frequency);
	
	public Map<String, Long> getMemberLoginCountOverTimeperiod(int startPeriod,
			int frequency);
	
	public Long getTotalPartnershipCount();
	
	public Long getTotalPartnerProgramCount();
	
	public AdminAccountDto login(String email, String password);
	
	public void createAdminAccount(AdminAccountDto account);
	
	public boolean isEmailExists(String email);
	
	public Long getTotalMemberCount();
	
	public Long getTotalPendingMemberCount();
	
	public Long getTotalSuspendedMemberCount();
	
	public Long getTotalActiveMemberCount();
	
	public Long getTotalBusinessCount();

}
