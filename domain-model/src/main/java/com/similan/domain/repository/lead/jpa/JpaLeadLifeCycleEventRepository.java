package com.similan.domain.repository.lead.jpa;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.lead.LeadLifeCycleEvent;
import com.similan.domain.entity.lead.LeadLifeCycleState;

public interface JpaLeadLifeCycleEventRepository extends
		JpaRepository<LeadLifeCycleEvent, Long> {

	@Query("select count(leadLifeCycleEvent) from LeadLifeCycleEvent leadLifeCycleEvent where (leadLifeCycleEvent.timeStamp between :fromDate and :toDate)")
	public abstract Long getCountLeadLifeCycleEventInDateRange(
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query("select count(leadLifeCycleEvent) from LeadLifeCycleEvent leadLifeCycleEvent where leadLifeCycleEvent.leadLifecycleState = :leadLifecycleState and  (leadLifeCycleEvent.timeStamp between :fromDate and :toDate)")
	public abstract Long getCountLeadLifeCycleEventInDateRange(
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("leadLifecycleState") LeadLifeCycleState leadLifeCycleState);

}
