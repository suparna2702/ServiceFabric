package com.similan.domain.repository.search;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.search.SearchEventType;
import com.similan.domain.entity.search.SearchStatisticsEvent;
import com.similan.domain.repository.search.jpa.JpaSearchStatisticsEventRepository;

@Repository
public class SearchStatisticsEventRepository {
  @Autowired
  private JpaSearchStatisticsEventRepository repository;
	
	public SearchStatisticsEvent save(SearchStatisticsEvent searchEvent) {
    return repository.save(searchEvent);
  }
	
	public SearchStatisticsEvent findOne(Long id) {
    return repository.findOne(id);
  }
	
	public List<SearchStatisticsEvent> findAll() {
    return repository.findAll();
  }
	
	public Long getSearchEventCountInDateRange(Date fromDate, Date toDate, 
			                                     SearchEventType eventType, Long forActorId) {
    return repository.getSearchEventCountInDateRange(fromDate, toDate, 
			                                     eventType, forActorId);
  }
	
	public Long getPartnerSearchEventCountInDateRange(Date fromDate, Date toDate, Long searchedOrgActorId) {
    return repository.getPartnerSearchEventCountInDateRange(fromDate, toDate, searchedOrgActorId);
  }
	
	public Long getPartnerSearchEventCountInDateRange(Date fromDate, Date toDate, Long searchedOrgActorId, Long partnerId) {
    return repository.getPartnerSearchEventCountInDateRange(fromDate, toDate, searchedOrgActorId, partnerId);
  }
	
	public Long getPartnerSearchEventCountInDateRangeByPartner(Date fromDate, Date toDate, Long partnerId) {
    return repository.getPartnerSearchEventCountInDateRangeByPartner(fromDate, toDate, partnerId);
  }
	
	public Long getSearchEventCountInDateRange(Date fromDate, Date toDate,
			SearchEventType communitysearch) {
    return repository.getSearchEventCountInDateRange(fromDate, toDate,
			communitysearch);
  }
	
	public SearchStatisticsEvent create(){
		SearchStatisticsEvent searchEvent = new SearchStatisticsEvent();
		return searchEvent;
	}

}
