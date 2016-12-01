package com.similan.domain.repository.lead.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.lead.CrmLeadSyncRecord;
import com.similan.domain.entity.lead.CrmSystemType;

public interface JpaCrmLeadSyncRecordRepository extends
		JpaRepository<CrmLeadSyncRecord, Long> {
	
	@Query("select crmLeadSyncRecord from CrmLeadSyncRecord crmLeadSyncRecord where crmLeadSyncRecord.owner=:owner and crmLeadSyncRecord.crmSource=:systemType order by timeStamp desc")
	public List<CrmLeadSyncRecord> findByOwnerOrderAndCrmSourceByTimeStampDesc(
			@Param("owner") SocialOrganization owner,
			@Param("systemType") CrmSystemType crmSystemType);
	}
