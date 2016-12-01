package com.similan.domain.repository.lead.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.lead.CrmRecordId;
import com.similan.domain.entity.lead.CrmRecordType;
import com.similan.domain.entity.lead.CrmSystemType;

public interface JpaCrmRecordIdRepository extends
		JpaRepository<CrmRecordId, Long> {

	public List<CrmRecordId> findByOwner(SocialOrganization owner);

	public List<CrmRecordId> findByOwnerAndCrmSourceAndCrmRecordType(
			SocialOrganization owner, CrmSystemType source,
			CrmRecordType type);

	public CrmRecordId findByLocalIdAndCrmSourceAndCrmRecordType(
			Long localId, CrmSystemType source, CrmRecordType type);

	public CrmRecordId findByRemoteIdAndCrmSourceAndCrmRecordType(
			String remoteId, CrmSystemType source, CrmRecordType type);
}
