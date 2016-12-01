package com.similan.domain.repository.lead.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.lead.FilteredAvailableLeadList;

public interface JpaFilteredAvailableLeadListRepository extends
		JpaRepository<FilteredAvailableLeadList, Long> {
	
	@Query("select filteredLeadList from FilteredAvailableLeadList filteredLeadList where filteredLeadList.forSocialActorId=:socialActorId")
	public FilteredAvailableLeadList findBySocialActor(@Param("socialActorId")Long socialActorId);

}
