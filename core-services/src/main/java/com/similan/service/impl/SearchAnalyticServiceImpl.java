package com.similan.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.search.SearchEventType;
import com.similan.domain.entity.search.SearchParameters;
import com.similan.domain.entity.search.SearchStatisticsEvent;
import com.similan.domain.repository.search.SearchStatisticsEventRepository;
import com.similan.framework.dto.DomainSearchResult;
import com.similan.framework.dto.DomainSearchResultSet;
import com.similan.framework.dto.SearchResultDto;
import com.similan.framework.dto.SearchResultItemDto;
import com.similan.framework.dto.SearchResultSummery;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.dto.search.SearchStatisticsDto;
import com.similan.service.api.SearchAnalyticService;

@Slf4j
public class SearchAnalyticServiceImpl implements SearchAnalyticService {
	
	
	@Autowired
	private SearchStatisticsEventRepository searchEventRepository;
	
	public List<SearchStatisticsDto> getSearchStatisticsByPeriod(int startPeriod, int frequency, Long actorId){
		
		if(startPeriod <= 0 || frequency <= 0 || actorId == null){
			return null;
		}
		
		
		List<SearchStatisticsDto> retSearchStat = new ArrayList<SearchStatisticsDto>();
		
		for(int i=0; (startPeriod - (i+1)*frequency) >= 0; i++){
			
			log.info("Search period from " + (startPeriod - i*frequency) 
					 + " to " +  (startPeriod - (i+1)*frequency));
			
			Calendar calFrom = GregorianCalendar.getInstance();
			calFrom.add( Calendar.DAY_OF_YEAR, (startPeriod - i*frequency)*(-1));
			Date fromDate = calFrom.getTime();
			
			Calendar calTo = GregorianCalendar.getInstance();
			calTo.add( Calendar.DAY_OF_YEAR, (startPeriod - (i+1)*frequency)*(-1));
			Date toDate = calTo.getTime();
			
			Long eventCountCoummunity = this.searchEventRepository.getSearchEventCountInDateRange(fromDate, 
					toDate, SearchEventType.CommunitySearch, actorId);
			log.info("Found search count for community " + eventCountCoummunity + " for actor " + actorId);
	        		
			Long eventCountAdvanced = this.searchEventRepository.getSearchEventCountInDateRange(fromDate, toDate, SearchEventType.AdvancedSearch, actorId);
			log.info("Found search count for advanced " + eventCountAdvanced + " for actor " + actorId);
			
			Long eventCountClickThrough = this.searchEventRepository.getSearchEventCountInDateRange(fromDate, toDate, SearchEventType.ClickThrough, actorId);
			log.info("Found search count for click through " + eventCountClickThrough + " for actor " + actorId);
			
			log.info("Search period from " + fromDate + " to " +  toDate);
			
			SearchStatisticsDto statisticsDto = new SearchStatisticsDto();
			statisticsDto.setPeriodInDays(frequency);
			statisticsDto.setSearched(eventCountCoummunity + eventCountAdvanced);
			statisticsDto.setViewed(eventCountClickThrough);
			
			retSearchStat.add(statisticsDto);
		}
		
		
		return retSearchStat;
	}
	
	public List<SearchStatisticsDto> getSearchStatistics(List<Integer> searchPeriod, Long actorId){
		if(searchPeriod == null){
			return null;
		}
		
		if(searchPeriod.size() <= 0){
			return null;
		}
		
		List<SearchStatisticsDto> retSearchStat = new ArrayList<SearchStatisticsDto>();
		Date toDate = new Date();
		
		for(Integer numDays : searchPeriod){
			
			Calendar cal = GregorianCalendar.getInstance();
			cal.add( Calendar.DAY_OF_YEAR, numDays*(-1));
			Date fromDate = cal.getTime();
			log.info("Date range for search from " + fromDate + " to date " + toDate);
			
			Long eventCountCoummunity = this.searchEventRepository.getSearchEventCountInDateRange(fromDate, toDate, SearchEventType.CommunitySearch, actorId);
			log.info("Found search count for community " + eventCountCoummunity + " for actor " + actorId);
			
			Long eventCountAdvanced = this.searchEventRepository.getSearchEventCountInDateRange(fromDate, toDate, SearchEventType.AdvancedSearch, actorId);
			log.info("Found search count for advanced " + eventCountAdvanced + " for actor " + actorId);
			
			Long eventCountClickThrough = this.searchEventRepository.getSearchEventCountInDateRange(fromDate, toDate, SearchEventType.ClickThrough, actorId);
			log.info("Found search count for click through " + eventCountClickThrough + " for actor " + actorId);
			
			SearchStatisticsDto statisticsDto = new SearchStatisticsDto();
			statisticsDto.setPeriodInDays(numDays);
			statisticsDto.setSearched(eventCountCoummunity + eventCountAdvanced);
			statisticsDto.setViewed(eventCountClickThrough);
			
			retSearchStat.add(statisticsDto);
		}
		
		return retSearchStat;
	}
	
	public void storeSearchEvents(SearchResultDto searchResult) {
		
		if(searchResult == null){
			return;
		}

		List<SearchResultItemDto> searchResultItems = searchResult
				.getSearchResults();
		SearchResultSummery resultSummery = searchResult.getResultSummary();

		for (SearchResultItemDto searchresultItem : searchResultItems) {
            SearchStatisticsEvent searchStatEvent = this.searchEventRepository.create();
            
            SearchParameters searchParams = new SearchParameters();
            searchParams.setTimeStamp(new Date());
            searchParams.setSearchRank(Long.valueOf(searchresultItem.getRank().toString()));
            searchParams.setSearcherActorId(searchResult.getFromSocialActorId());
            searchParams.setSearchResultActorId(searchresultItem.getForSocialActorId());
            searchParams.setSearchString(resultSummery.getSearchString());
            searchParams.setTotalSearchResults(Long.valueOf(String.valueOf(resultSummery.getTotalRecords())));
            searchParams.setSearchEventType(SearchEventType.CommunitySearch);
            
            searchStatEvent.setSearchParams(searchParams);
            this.searchEventRepository.save(searchStatEvent);
		}
	}

	public void storeClickThroughEvent(LeadDto lead) {
		if(lead == null){
			return;
		}
		
		SearchStatisticsEvent searchStatEvent = this.searchEventRepository.create();
		
		SearchParameters searchParams = new SearchParameters();
        searchParams.setTimeStamp(new Date());
        searchParams.setSearcherActorId(lead.getFromSocialActorId());
        searchParams.setSearchResultActorId(lead.getForSocialActorId());
        searchParams.setSearchEventType(SearchEventType.ClickThrough);
         
        searchStatEvent.setSearchParams(searchParams);
        this.searchEventRepository.save(searchStatEvent);
		
	}

	public void storePartnerSearchEvent(Long orgId, DomainSearchResultSet resultSet) {
		if(resultSet == null){
			return;
		}
		
		for(DomainSearchResult searchResult : resultSet.getSearchResultList()){
			
			SearchStatisticsEvent searchStatEvent = this.searchEventRepository.create();
			
			SearchParameters searchParams = new SearchParameters();
	        searchParams.setTimeStamp(new Date());
	        searchParams.setSearcherActorId(resultSet.getSercherId());
	        searchParams.setSearchResultActorId(orgId);
	        searchParams.setPartnerActorId(searchResult.getOrgInfo().getId());
	        searchParams.setSearchEventType(SearchEventType.PartnerSearch);
	         
	        searchStatEvent.setSearchParams(searchParams);
	        this.searchEventRepository.save(searchStatEvent);
			
		}
		
	}
}
