package com.similan.domain.repository.lead.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.lead.SearchLead;

public interface JpaSearchLeadRepository 
                 extends JpaRepository<SearchLead, Long>{
	
	@Query("select lead from SearchLead lead where lead.forSocialActorId=:actorId")
	List<SearchLead> findLeadsForSocialActor(@Param("actorId")Long actorId);
	
	@Query("select count(lead) from SearchLead lead where lead.forSocialActorId=:actorId")
	Long getLeadCountSocialActor(@Param("actorId")Long actorId);

	@Query("select count(lead) from SearchLead lead where lead.forSocialActorId=:actorId and viewed = false")
	Long getNotViewedLeadCountSocialActor(@Param("actorId")Long actorId);
}
