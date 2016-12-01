package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.lead.CrmLeadSyncRecord;
import com.similan.domain.entity.lead.CrmSystemType;
import com.similan.domain.repository.lead.jpa.JpaCrmLeadSyncRecordRepository;

@Repository
public class CrmLeadSyncRecordRepository {
  @Autowired
  private JpaCrmLeadSyncRecordRepository repository;

	public CrmLeadSyncRecord save(CrmLeadSyncRecord crmLeadSyncRecord) {
    return repository.save(crmLeadSyncRecord);
  }

	public CrmLeadSyncRecord findOne(Long id) {
    return repository.findOne(id);
  }

	public List<CrmLeadSyncRecord> findAll() {
    return repository.findAll();
  }

	public List<CrmLeadSyncRecord> findByOwnerOrderAndCrmSourceByTimeStampDesc(SocialOrganization owner,
			CrmSystemType crmSystemType) {
    return repository.findByOwnerOrderAndCrmSourceByTimeStampDesc(owner,
			crmSystemType);
  }
	
	public CrmLeadSyncRecord create() {
		CrmLeadSyncRecord crmLeadSyncRecord = new CrmLeadSyncRecord();
		return crmLeadSyncRecord;
	}


}
