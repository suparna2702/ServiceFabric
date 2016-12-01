package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.Lead;
import com.similan.domain.entity.lead.TransferLead;
import com.similan.domain.repository.lead.jpa.JpaTransferLeadRepository;

@Repository
public class TransferLeadRepository {
  @Autowired
  private JpaTransferLeadRepository repository;

	public TransferLead findOne(Long id) {
    return repository.findOne(id);
  }

	public List<TransferLead> findAll() {
    return repository.findAll();
  }

	public List<TransferLead> findLeadsForSocialActor(Long actorId) {
    return repository.findLeadsForSocialActor(actorId);
  }

	public TransferLead save(TransferLead message) {
    return repository.save(message);
  }

	public Long getLeadCount() {
    return repository.getLeadCount();
  }

	public Long getLeadCountSocialActor(Long actorId) {
    return repository.getLeadCountSocialActor(actorId);
  }

	public TransferLead create() {
		TransferLead transferLead = new TransferLead();
		return transferLead;
	}

	public List<TransferLead> findAcceptedOrPendingLeadsForSocialActor(
			Long socialActorId) {
    return repository.findAcceptedOrPendingLeadsForSocialActor(
			socialActorId);
  }

	public Long getNotViewedLeadCountSocialActor(Long id) {
    return repository.getNotViewedLeadCountSocialActor(id);
  }
	
	public List<TransferLead> findActiveByParent(Lead parentLead) {
    return repository.findActiveByParent(parentLead);
  }

	public List<TransferLead> findActiveOrPendingByParent(Lead parentLead) {
    return repository.findActiveOrPendingByParent(parentLead);
  }
}
