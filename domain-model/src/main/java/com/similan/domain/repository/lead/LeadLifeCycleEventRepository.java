package com.similan.domain.repository.lead;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.LeadCrmSynchronizationEvent;
import com.similan.domain.entity.lead.LeadDeletedEvent;
import com.similan.domain.entity.lead.LeadImportedEvent;
import com.similan.domain.entity.lead.LeadLifeCycleEvent;
import com.similan.domain.entity.lead.LeadLifeCycleState;
import com.similan.domain.entity.lead.LeadTransferredEvent;
import com.similan.domain.repository.lead.jpa.JpaLeadLifeCycleEventRepository;

@Repository
public class LeadLifeCycleEventRepository {
  @Autowired
  private JpaLeadLifeCycleEventRepository repository;
	
	public LeadLifeCycleEvent findOne(Long id) {
    return repository.findOne(id);
  }
	
	public List<LeadLifeCycleEvent> findAll() {
    return repository.findAll();
  }
	
	public LeadLifeCycleEvent save(LeadLifeCycleEvent leadLifeCycleEvent) {
    return repository.save(leadLifeCycleEvent);
  }

	public Long getCountLeadLifeCycleEventInDateRange(Date fromDate, Date toDate) {
    return repository.getCountLeadLifeCycleEventInDateRange(fromDate, toDate);
  }

	public Long getCountLeadLifeCycleEventInDateRange(Date fromDate, Date toDate, LeadLifeCycleState leadLifeCycleState) {
    return repository.getCountLeadLifeCycleEventInDateRange(fromDate, toDate, leadLifeCycleState);
  }
	
	public LeadTransferredEvent createLeadTransferredEvent(){
		LeadTransferredEvent leadTransferredEvent = 
				new LeadTransferredEvent();
		leadTransferredEvent.setLeadLifecycleState(LeadLifeCycleState.Transferred);
		return leadTransferredEvent;
	}
	
	public LeadImportedEvent createLeadImportedEvent(){
		LeadImportedEvent leadImportedEvent = 
				new LeadImportedEvent();
		leadImportedEvent.setLeadLifecycleState(LeadLifeCycleState.Imported);
		return leadImportedEvent;
	}	

	public LeadCrmSynchronizationEvent createLeadCrmSynchronizationEvent(){
		LeadCrmSynchronizationEvent leadCrmSynchronizationEvent = 
				new LeadCrmSynchronizationEvent();
		leadCrmSynchronizationEvent.setLeadLifecycleState(LeadLifeCycleState.Synced);
		return leadCrmSynchronizationEvent;
	}	
	
	public LeadDeletedEvent createLeadDeletedEvent(){
		LeadDeletedEvent leadDeletedEvent = 
				new LeadDeletedEvent();
		leadDeletedEvent.setLeadLifecycleState(LeadLifeCycleState.Deleted);
		return leadDeletedEvent;
	}	
}
