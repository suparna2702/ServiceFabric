package com.similan.service.api;

import java.util.List;

import com.similan.framework.dto.DomainSearchResultSet;
import com.similan.framework.dto.SearchResultDto;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.dto.search.SearchStatisticsDto;

public interface SearchAnalyticService {
	
	public List<SearchStatisticsDto> getSearchStatisticsByPeriod(int startPeriod, int frequency, Long actorId);
	
	public List<SearchStatisticsDto> getSearchStatistics(List<Integer> searchPeriod, Long actorId);
	
	public void storeSearchEvents(SearchResultDto searchResult);
	
	public void storeClickThroughEvent(LeadDto lead);
	
	public void storePartnerSearchEvent(Long orgId, DomainSearchResultSet resultSet);

}
