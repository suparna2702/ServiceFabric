package com.similan.portal.service;

import java.util.List;

import com.similan.framework.dto.search.SearchStatisticsDto;

public interface SearchService {

	List<SearchStatisticsDto> getSearchStatistics(List<Integer> periodList, Long actorId);
	
	public List<SearchStatisticsDto> getSearchStatisticsByPeriod(int startPeriod, int frequency, Long actorId);

}
