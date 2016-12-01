package com.similan.domain.repository.lead.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.lead.AcquiredLead;

public interface JpaAcquiredLeadRepository 
                 extends JpaRepository<AcquiredLead, Long> {
	
	@Query("select count(lead) from AcquiredLead lead")
	Long getLeadCount();
	
	@Query("select lead from AcquiredLead lead where lead.forSocialActorId=:actorId")
	List<AcquiredLead> findLeadsForSocialActor(@Param("actorId")Long actorId);
	
	@Query("select count(lead) from AcquiredLead lead where lead.forSocialActorId=:actorId and viewed = false")
	Long getNotViewedLeadCountSocialActor(@Param("actorId")Long actorId);
	
	@Query("select count(lead) from AcquiredLead lead where lead.forSocialActorId=:actorId")
	public Long getLeadCountSocialActor(@Param("actorId")long actorId);
}
