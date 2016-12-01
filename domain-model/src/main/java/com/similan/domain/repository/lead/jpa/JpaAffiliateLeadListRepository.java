package com.similan.domain.repository.lead.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.lead.AffiliateLeadList;

public interface JpaAffiliateLeadListRepository 
             extends JpaRepository<AffiliateLeadList, Long> {
	
	@Query("select leadList from AffiliateLeadList leadList where leadList.affiliateId=:ownerId")
	public List<AffiliateLeadList> findByOwner(@Param("ownerId")Long ownerId);

}
