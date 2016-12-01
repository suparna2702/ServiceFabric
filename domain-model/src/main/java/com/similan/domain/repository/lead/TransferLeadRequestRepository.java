package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.TransferLeadRequest;
import com.similan.domain.repository.lead.jpa.JpaTransferLeadRequestRepository;

@Repository
public class TransferLeadRequestRepository {
  @Autowired
  private JpaTransferLeadRequestRepository repository;

	public TransferLeadRequest findOne(Long id) {
    return repository.findOne(id);
  }

	public List<TransferLeadRequest> findAll() {
    return repository.findAll();
  }

	public List<TransferLeadRequest> findLeadsForSocialActor(Long actorId) {
    return repository.findLeadsForSocialActor(actorId);
  }

	public TransferLeadRequest save(TransferLeadRequest message) {
    return repository.save(message);
  }

	public TransferLeadRequest create() {
		TransferLeadRequest transferLeadRequest = new TransferLeadRequest();
		return transferLeadRequest;
	}
}
