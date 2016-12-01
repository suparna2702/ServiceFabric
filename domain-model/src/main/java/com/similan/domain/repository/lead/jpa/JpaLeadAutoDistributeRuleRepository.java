package com.similan.domain.repository.lead.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.lead.Lead;
import com.similan.domain.entity.lead.LeadAutoDistributeRule;

public interface JpaLeadAutoDistributeRuleRepository extends
		JpaRepository<LeadAutoDistributeRule, Long> {
	
	@Query("select leadAutoDistributeRule from LeadAutoDistributeRule leadAutoDistributeRule where leadAutoDistributeRule.owner=:owner")
	public List<LeadAutoDistributeRule> getByOwner(@Param("owner")Lead owner);

}
