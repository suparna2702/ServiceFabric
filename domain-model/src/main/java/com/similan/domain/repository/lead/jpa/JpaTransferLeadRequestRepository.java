package com.similan.domain.repository.lead.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.lead.TransferLeadRequest;

public interface JpaTransferLeadRequestRepository extends
		JpaRepository<TransferLeadRequest, Long> {

	@Query("select request from TransferLeadRequest request where request.toSocialActorId=:actorId")
	List<TransferLeadRequest> findLeadsForSocialActor(@Param("actorId") Long actorId);
}
