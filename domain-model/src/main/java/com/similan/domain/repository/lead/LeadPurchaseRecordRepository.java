package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.acccount.LeadPurchaseRecord;
import com.similan.domain.repository.lead.jpa.JpaLeadPurchaseRecordRepository;

@Repository
public class LeadPurchaseRecordRepository {
  @Autowired
  private JpaLeadPurchaseRecordRepository repository;
	
	public LeadPurchaseRecord save(LeadPurchaseRecord accRecord) {
    return repository.save(accRecord);
  }
	
	public List<LeadPurchaseRecord> findAll() {
    return repository.findAll();
  }
	
	public LeadPurchaseRecord findOne(Long id) {
    return repository.findOne(id);
  }
	
	public LeadPurchaseRecord create(){
		LeadPurchaseRecord accRecord = new LeadPurchaseRecord();
		return accRecord;
	}

}
