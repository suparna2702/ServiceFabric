package com.similan.service.api;

import java.util.Map;

public interface CustomerEngagementAnalyticsService {
	
	public Map<String, Long> getPollEventCountOverTimeperiod(int startPeriod,
			int frequency, Long orgId);
	
	public Map<String, Long> getPollSubmissionCountOverTimeperiod(int startPeriod,
			int frequency, Long orgId);
	
	public Map<String, Long> getPartnerSearchEventCountOverTimeperiodByOrg(
			int startPeriod, int frequency, Long orgId);
	
	public Map<String, Long> getPollSubmissionCountOverTimeperiod(int startPeriod,
			int frequency);
	
	public Map<String, Long> getPollEventCountOverTimeperiod(int startPeriod,
			int frequency);
	
	public Map<String, Long> getMemberLoginCountOverTimeperiod(int startPeriod, int frequency);
	
	public Map<String, Long> getWallEventCountOverTimeperiod(int startPeriod, int frequency);
	
	public Map<String, Long> getCommunitySearchEventCountOverTimeperiod(int startPeriod, int frequency);
	
	public Map<String, Long> getPartnerSearchEventCountOverTimeperiod(int startPeriod, int frequency);
	
	public Map<String, Long> getInviteCountOverTimeperiod(int startPeriod, int frequency);
	
	public Map<String, Long> getLeadTransferCountOverTimeperiod(int startPeriod, int frequency);
	
	public Map<String, Long> getCrmSyncCountOverTimeperiod(int startPeriod, int frequency);
	
	public Map<String, Long> getLeadImportCountOverTimeperiod(int startPeriod, int frequency);
	
	

}
