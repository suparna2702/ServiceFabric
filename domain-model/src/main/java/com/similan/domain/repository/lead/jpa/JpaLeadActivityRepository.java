package com.similan.domain.repository.lead.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.lead.Lead;
import com.similan.domain.entity.lead.LeadActivity;

public interface JpaLeadActivityRepository extends JpaRepository<LeadActivity, Long> {
	
	@Query("select leadActivity from LeadActivity leadActivity where leadActivity.owner=:owner")
	public abstract List<LeadActivity> getLeadActivityByOwner(@Param("owner")Lead owner);
	
	@Query("select leadActivity from LeadActivity leadActivity where leadActivity.owner.forSocialActorId=:orgId")
	public abstract List<LeadActivity> getLeadActivityByOrganization(@Param("orgId")Long orgId);
	
	@Query("delete from LeadActivity leadActivity where leadActivity.id=:id")
	public abstract void delete(@Param("id")Long id);

}
