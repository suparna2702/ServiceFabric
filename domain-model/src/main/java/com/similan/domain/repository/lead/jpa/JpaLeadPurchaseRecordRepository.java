package com.similan.domain.repository.lead.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.acccount.LeadPurchaseRecord;

public interface JpaLeadPurchaseRecordRepository 
                 extends JpaRepository<LeadPurchaseRecord, Long> {

}
