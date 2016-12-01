package com.similan.portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.framework.dto.search.SearchStatisticsDto;
import com.similan.service.api.SearchAnalyticService;

@Service("searchService")
public class SearchServiceImpl extends BaseService implements SearchService {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SearchAnalyticService searchAnalyticService;

	public SearchAnalyticService getSearchAnalyticService() {
		return searchAnalyticService;
	}

	public void setSearchAnalyticService(SearchAnalyticService searchAnalyticService) {
		this.searchAnalyticService = searchAnalyticService;
	}
	
	
	@Transactional
	public List<SearchStatisticsDto> getSearchStatistics(List<Integer> periodList, Long actorId){
		return this.searchAnalyticService.getSearchStatistics(periodList, actorId);
	}
	
	public List<SearchStatisticsDto> getSearchStatisticsByPeriod(int startPeriod, int frequency, Long actorId){
		return this.searchAnalyticService.getSearchStatisticsByPeriod(startPeriod, frequency, actorId);
	}
	
	

}
