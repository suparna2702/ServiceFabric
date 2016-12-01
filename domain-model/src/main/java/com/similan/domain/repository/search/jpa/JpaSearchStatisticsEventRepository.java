package com.similan.domain.repository.search.jpa;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.search.SearchEventType;
import com.similan.domain.entity.search.SearchStatisticsEvent;

public interface JpaSearchStatisticsEventRepository 
                               extends JpaRepository<SearchStatisticsEvent, Long>{
	
	@Query("select count(searchEvent) from SearchStatisticsEvent searchEvent where (searchEvent.searchParams.timeStamp between :fromDate and :toDate) " +
			"and (searchEvent.searchParams.searchEventType=:searchEventType) " +
			"and (searchEvent.searchParams.searchResultActorId=:forActorId)")
	public Long getSearchEventCountInDateRange(@Param("fromDate")Date fromDate, 
			                                   @Param("toDate")Date toDate, 
			                                   @Param("searchEventType")SearchEventType searchEventType,
			                                   @Param("forActorId")Long forActorId);
	
	@Query("select count(searchEvent) from SearchStatisticsEvent searchEvent where (searchEvent.searchParams.timeStamp between :fromDate and :toDate) " +
			"and (searchEvent.searchParams.searchEventType=:searchEventType)")
	public Long getSearchEventCountInDateRange(@Param("fromDate")Date fromDate, 
            @Param("toDate")Date toDate, 
            @Param("searchEventType")SearchEventType searchEventType);
	
	@Query("select count(searchEvent) from SearchStatisticsEvent searchEvent where (searchEvent.searchParams.timeStamp between :fromDate and :toDate) " +
			"and (searchEvent.searchParams.searchResultActorId=:searchedOrgActorId)")
	public Long getPartnerSearchEventCountInDateRange(@Param("fromDate")Date fromDate, 
                                                            @Param("toDate")Date toDate,
                                                            @Param("searchedOrgActorId")Long searchedOrgActorId);
	
	@Query("select count(searchEvent) from SearchStatisticsEvent searchEvent where (searchEvent.searchParams.timeStamp between :fromDate and :toDate) " +
			"and (searchEvent.searchParams.searchResultActorId=:searchedOrgActorId) " +
			"and (searchEvent.searchParams.partnerActorId=:partnerId)")
	public Long getPartnerSearchEventCountInDateRange(@Param("fromDate")Date fromDate, 
                                                      @Param("toDate")Date toDate, 
                                                      @Param("searchedOrgActorId")Long searchedOrgActorId, @Param("partnerId")Long partnerId);
	
	@Query("select count(searchEvent) from SearchStatisticsEvent searchEvent where (searchEvent.searchParams.timeStamp between :fromDate and :toDate) " +
			"and (searchEvent.searchParams.partnerActorId=:partnerId)")
	public Long getPartnerSearchEventCountInDateRangeByPartner(@Param("fromDate")Date fromDate, 
            @Param("toDate")Date toDate, 
            @Param("partnerId")Long partnerId);

}
