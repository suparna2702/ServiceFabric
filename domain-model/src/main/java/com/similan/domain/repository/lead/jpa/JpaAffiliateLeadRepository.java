package com.similan.domain.repository.lead.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.lead.AffiliateLead;

public interface JpaAffiliateLeadRepository extends JpaRepository<AffiliateLead, Long> {
	
	@Query("select affiliateLead from AffiliateLead affiliateLead where affiliateLead.affiliateId=:ownerId")
	public List<AffiliateLead> findAllByOwner(@Param("ownerId")Long ownerId);

}
