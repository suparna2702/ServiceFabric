package com.similan.domain.repository.lead.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.lead.Lead;
import com.similan.domain.entity.lead.TransferLead;

public interface JpaTransferLeadRepository extends
		JpaRepository<TransferLead, Long> {

	@Query("select lead from TransferLead lead where lead.forSocialActorId=:actorId")
	List<TransferLead> findLeadsForSocialActor(@Param("actorId") Long actorId);

	@Query("select count(lead) from TransferLead lead")
	Long getLeadCount();

	@Query("select count(lead) from TransferLead lead where lead.forSocialActorId=:actorId")
	Long getLeadCountSocialActor(@Param("actorId") Long actorId);

	@Query("select lead from TransferLead lead where lead.forSocialActorId=:actorId and (lead.transferState = com.similan.domain.entity.lead.TransferState.Pending or lead.transferState = com.similan.domain.entity.lead.TransferState.Accepted)")
	List<TransferLead> findAcceptedOrPendingLeadsForSocialActor(
			@Param("actorId") Long socialActorId);
	
	@Query("select count(lead) from TransferLead lead where lead.forSocialActorId=:actorId and viewed = false")
	Long getNotViewedLeadCountSocialActor(@Param("actorId")Long actorId);
	
	@Query("select lead from TransferLead lead where lead.parentLead=:parent and lead.transferState = com.similan.domain.entity.lead.TransferState.Accepted")
	List<TransferLead> findActiveByParent(@Param("parent") Lead parent);

	@Query("select lead from TransferLead lead where lead.parentLead=:parent and (lead.transferState = com.similan.domain.entity.lead.TransferState.Pending or lead.transferState = com.similan.domain.entity.lead.TransferState.Accepted)")
	List<TransferLead> findActiveOrPendingByParent(@Param("parent") Lead parent);
}
