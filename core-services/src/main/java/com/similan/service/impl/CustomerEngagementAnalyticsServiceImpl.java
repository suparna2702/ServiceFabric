package com.similan.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.lead.LeadLifeCycleState;
import com.similan.domain.entity.search.SearchEventType;
import com.similan.domain.repository.event.MemberLoginEventRepository;
import com.similan.domain.repository.lead.LeadLifeCycleEventRepository;
import com.similan.domain.repository.poll.PollEventRepository;
import com.similan.domain.repository.poll.PollSubmissionRepository;
import com.similan.domain.repository.search.SearchStatisticsEventRepository;
import com.similan.service.api.CustomerEngagementAnalyticsService;

@Slf4j
public class CustomerEngagementAnalyticsServiceImpl implements
		CustomerEngagementAnalyticsService {
	
	
	@Autowired
	private MemberLoginEventRepository loginEventRepository;
	
	@Autowired
	private SearchStatisticsEventRepository searchEventRepository;
	
	@Autowired
	private PollEventRepository pollEventRepository;
	
	@Autowired
	private PollSubmissionRepository pollSubmissionRepository;
	
	@Autowired
	private LeadLifeCycleEventRepository leadLifeCycleRepository;
	
	public Map<String, Long> getPollSubmissionCountOverTimeperiod(int startPeriod,
			int frequency, Long orgId) {
		
		Map<String, Long> loginMap = new HashMap<String, Long>();		
		for(int i=0; (startPeriod - (i+1)*frequency) >= 0; i++){
			
			log.info("Search period from " + (startPeriod - i*frequency) 
					 + " to " +  (startPeriod - (i+1)*frequency));
			
			Calendar calFrom = GregorianCalendar.getInstance();
			calFrom.add( Calendar.DAY_OF_YEAR, (startPeriod - i*frequency)*(-1));
			Date fromDate = calFrom.getTime();
			
			Calendar calTo = GregorianCalendar.getInstance();
			calTo.add( Calendar.DAY_OF_YEAR, (startPeriod - (i+1)*frequency)*(-1));
			Date toDate = calTo.getTime();
			
			Long loginCount = this.pollSubmissionRepository.getPollSubmissionEventCountInDateRange(fromDate, toDate, orgId);
			loginMap.put(fromDate.toString(), loginCount);
		}
		
		return loginMap;
	}
	
	public Map<String, Long> getPollSubmissionCountOverTimeperiod(int startPeriod,
			int frequency) {
		
		Map<String, Long> loginMap = new HashMap<String, Long>();		
		for(int i=0; (startPeriod - (i+1)*frequency) >= 0; i++){
			
			log.info("Search period from " + (startPeriod - i*frequency) 
					 + " to " +  (startPeriod - (i+1)*frequency));
			
			Calendar calFrom = GregorianCalendar.getInstance();
			calFrom.add( Calendar.DAY_OF_YEAR, (startPeriod - i*frequency)*(-1));
			Date fromDate = calFrom.getTime();
			
			Calendar calTo = GregorianCalendar.getInstance();
			calTo.add( Calendar.DAY_OF_YEAR, (startPeriod - (i+1)*frequency)*(-1));
			Date toDate = calTo.getTime();
			
			Long loginCount = this.pollSubmissionRepository.getPollSubmissionEventCountInDateRange(fromDate, toDate);
			loginMap.put(fromDate.toString(), loginCount);
		}
		
		return loginMap;
	}
	
	public Map<String, Long> getPollEventCountOverTimeperiod(int startPeriod,
			int frequency, Long orgId) {
		
		Map<String, Long> loginMap = new HashMap<String, Long>();		
		for(int i=0; (startPeriod - (i+1)*frequency) >= 0; i++){
			
			log.info("Search period from " + (startPeriod - i*frequency) 
					 + " to " +  (startPeriod - (i+1)*frequency));
			
			Calendar calFrom = GregorianCalendar.getInstance();
			calFrom.add( Calendar.DAY_OF_YEAR, (startPeriod - i*frequency)*(-1));
			Date fromDate = calFrom.getTime();
			
			Calendar calTo = GregorianCalendar.getInstance();
			calTo.add( Calendar.DAY_OF_YEAR, (startPeriod - (i+1)*frequency)*(-1));
			Date toDate = calTo.getTime();
			
			Long loginCount = this.pollEventRepository.getPollEventCountInDateRange(fromDate, toDate, orgId);
			loginMap.put(fromDate.toString(), loginCount);
		}
		
		return loginMap;
	}
	
	public Map<String, Long> getPollEventCountOverTimeperiod(int startPeriod,
			int frequency) {
		
		Map<String, Long> loginMap = new HashMap<String, Long>();		
		for(int i=0; (startPeriod - (i+1)*frequency) >= 0; i++){
			
			log.info("Search period from " + (startPeriod - i*frequency) 
					 + " to " +  (startPeriod - (i+1)*frequency));
			
			Calendar calFrom = GregorianCalendar.getInstance();
			calFrom.add( Calendar.DAY_OF_YEAR, (startPeriod - i*frequency)*(-1));
			Date fromDate = calFrom.getTime();
			
			Calendar calTo = GregorianCalendar.getInstance();
			calTo.add( Calendar.DAY_OF_YEAR, (startPeriod - (i+1)*frequency)*(-1));
			Date toDate = calTo.getTime();
			
			Long loginCount = this.pollEventRepository.getPollEventCountInDateRange(fromDate, toDate);
			loginMap.put(fromDate.toString(), loginCount);
		}
		
		return loginMap;
	}

	public Map<String, Long> getMemberLoginCountOverTimeperiod(int startPeriod,
			int frequency) {
		
		Map<String, Long> loginMap = new HashMap<String, Long>();		
		for(int i=0; (startPeriod - (i+1)*frequency) >= 0; i++){
			
			log.info("Search period from " + (startPeriod - i*frequency) 
					 + " to " +  (startPeriod - (i+1)*frequency));
			
			Calendar calFrom = GregorianCalendar.getInstance();
			calFrom.add( Calendar.DAY_OF_YEAR, (startPeriod - i*frequency)*(-1));
			Date fromDate = calFrom.getTime();
			
			Calendar calTo = GregorianCalendar.getInstance();
			calTo.add( Calendar.DAY_OF_YEAR, (startPeriod - (i+1)*frequency)*(-1));
			Date toDate = calTo.getTime();
			
			Long loginCount = this.loginEventRepository.getLoginEventCountInDateRange(fromDate, toDate);
			loginMap.put(fromDate.toString(), loginCount);
		}
		
		return loginMap;
	}

	public Map<String, Long> getWallEventCountOverTimeperiod(int startPeriod,
			int frequency) {
    throw new UnsupportedOperationException();
	}

	public Map<String, Long> getCommunitySearchEventCountOverTimeperiod(
			int startPeriod, int frequency) {
		
		Map<String, Long> loginMap = new HashMap<String, Long>();		
		for(int i=0; (startPeriod - (i+1)*frequency) >= 0; i++){
			
			log.info("Search period from " + (startPeriod - i*frequency) 
					 + " to " +  (startPeriod - (i+1)*frequency));
			
			Calendar calFrom = GregorianCalendar.getInstance();
			calFrom.add( Calendar.DAY_OF_YEAR, (startPeriod - i*frequency)*(-1));
			Date fromDate = calFrom.getTime();
			
			Calendar calTo = GregorianCalendar.getInstance();
			calTo.add( Calendar.DAY_OF_YEAR, (startPeriod - (i+1)*frequency)*(-1));
			Date toDate = calTo.getTime();
			
			Long communitySearchCount = this.searchEventRepository.getSearchEventCountInDateRange(fromDate, toDate, 
					SearchEventType.CommunitySearch);
			loginMap.put(fromDate.toString(), communitySearchCount);
		}
		
		return loginMap;
	}

	public Map<String, Long> getPartnerSearchEventCountOverTimeperiod(
			int startPeriod, int frequency) {
		
		Map<String, Long> loginMap = new HashMap<String, Long>();		
		for(int i=0; (startPeriod - (i+1)*frequency) >= 0; i++){
			
			log.info("Search period from " + (startPeriod - i*frequency) 
					 + " to " +  (startPeriod - (i+1)*frequency));
			
			Calendar calFrom = GregorianCalendar.getInstance();
			calFrom.add( Calendar.DAY_OF_YEAR, (startPeriod - i*frequency)*(-1));
			Date fromDate = calFrom.getTime();
			
			Calendar calTo = GregorianCalendar.getInstance();
			calTo.add( Calendar.DAY_OF_YEAR, (startPeriod - (i+1)*frequency)*(-1));
			Date toDate = calTo.getTime();
			
			Long partnerSearchCount = this.searchEventRepository.getSearchEventCountInDateRange(fromDate, toDate, 
					SearchEventType.PartnerSearch);
			loginMap.put(fromDate.toString(), partnerSearchCount);
		}
		
		return loginMap;
	}
	
	public Map<String, Long> getPartnerSearchEventCountOverTimeperiodByOrg(
			int startPeriod, int frequency, Long orgId) {
		
		Map<String, Long> loginMap = new HashMap<String, Long>();		
		for(int i=0; (startPeriod - (i+1)*frequency) >= 0; i++){
			
			log.info("Search period from " + (startPeriod - i*frequency) 
					 + " to " +  (startPeriod - (i+1)*frequency));
			
			Calendar calFrom = GregorianCalendar.getInstance();
			calFrom.add( Calendar.DAY_OF_YEAR, (startPeriod - i*frequency)*(-1));
			Date fromDate = calFrom.getTime();
			
			Calendar calTo = GregorianCalendar.getInstance();
			calTo.add( Calendar.DAY_OF_YEAR, (startPeriod - (i+1)*frequency)*(-1));
			Date toDate = calTo.getTime();
			
			Long partnerSearchCount = this.searchEventRepository.getPartnerSearchEventCountInDateRange(fromDate, toDate, 
					orgId);
			loginMap.put(fromDate.toString(), partnerSearchCount);
		}
		
		return loginMap;
	}

	public Map<String, Long> getInviteCountOverTimeperiod(int startPeriod,
			int frequency) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Long> getLeadTransferCountOverTimeperiod(
			int startPeriod, int frequency) {
		
		Map<String, Long> leadTransferMap = new HashMap<String, Long>();		
		for(int i=0; (startPeriod - (i+1)*frequency) >= 0; i++){
			
			log.info("Search period from " + (startPeriod - i*frequency) 
					 + " to " +  (startPeriod - (i+1)*frequency));
			
			Calendar calFrom = GregorianCalendar.getInstance();
			calFrom.add( Calendar.DAY_OF_YEAR, (startPeriod - i*frequency)*(-1));
			Date fromDate = calFrom.getTime();
			
			Calendar calTo = GregorianCalendar.getInstance();
			calTo.add( Calendar.DAY_OF_YEAR, (startPeriod - (i+1)*frequency)*(-1));
			Date toDate = calTo.getTime();
			
			Long leadTransferCount = this.leadLifeCycleRepository.getCountLeadLifeCycleEventInDateRange(fromDate, toDate, LeadLifeCycleState.Transferred);
			leadTransferMap.put(fromDate.toString(), leadTransferCount);
		}
		
		return leadTransferMap;
	}

	public Map<String, Long> getCrmSyncCountOverTimeperiod(int startPeriod,
			int frequency) {
		
		Map<String, Long> leadSyncMap = new HashMap<String, Long>();		
		for(int i=0; (startPeriod - (i+1)*frequency) >= 0; i++){
			
			log.info("Search period from " + (startPeriod - i*frequency) 
					 + " to " +  (startPeriod - (i+1)*frequency));
			
			Calendar calFrom = GregorianCalendar.getInstance();
			calFrom.add( Calendar.DAY_OF_YEAR, (startPeriod - i*frequency)*(-1));
			Date fromDate = calFrom.getTime();
			
			Calendar calTo = GregorianCalendar.getInstance();
			calTo.add( Calendar.DAY_OF_YEAR, (startPeriod - (i+1)*frequency)*(-1));
			Date toDate = calTo.getTime();
			
			Long leadSyncCount = this.leadLifeCycleRepository.getCountLeadLifeCycleEventInDateRange(fromDate, toDate, LeadLifeCycleState.Synced);
			leadSyncMap.put(fromDate.toString(), leadSyncCount);
		}
		
		return leadSyncMap;
	}

	public Map<String, Long> getLeadImportCountOverTimeperiod(int startPeriod,
			int frequency) {
		
		Map<String, Long> leadImportMap = new HashMap<String, Long>();		
		for(int i=0; (startPeriod - (i+1)*frequency) >= 0; i++){
			
			log.info("Search period from " + (startPeriod - i*frequency) 
					 + " to " +  (startPeriod - (i+1)*frequency));
			
			Calendar calFrom = GregorianCalendar.getInstance();
			calFrom.add( Calendar.DAY_OF_YEAR, (startPeriod - i*frequency)*(-1));
			Date fromDate = calFrom.getTime();
			
			Calendar calTo = GregorianCalendar.getInstance();
			calTo.add( Calendar.DAY_OF_YEAR, (startPeriod - (i+1)*frequency)*(-1));
			Date toDate = calTo.getTime();
			
			Long leadImportCount = this.leadLifeCycleRepository.getCountLeadLifeCycleEventInDateRange(fromDate, toDate, LeadLifeCycleState.Synced);
			leadImportMap.put(fromDate.toString(), leadImportCount);
		}
		
		return leadImportMap;
	}

}
