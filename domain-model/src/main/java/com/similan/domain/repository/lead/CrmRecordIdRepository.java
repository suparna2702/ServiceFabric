package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.lead.CrmRecordId;
import com.similan.domain.entity.lead.CrmRecordType;
import com.similan.domain.entity.lead.CrmSystemType;
import com.similan.domain.repository.lead.jpa.JpaCrmRecordIdRepository;

@Repository
public class CrmRecordIdRepository {
  @Autowired
  private JpaCrmRecordIdRepository repository;

	public CrmRecordId save(CrmRecordId crmRecordId) {
    return repository.save(crmRecordId);
  }

	public CrmRecordId findOne(Long id) {
    return repository.findOne(id);
  }

	public List<CrmRecordId> findAll() {
    return repository.findAll();
  }
	
	public List<CrmRecordId> findByOwner(SocialOrganization owner) {
    return repository.findByOwner(owner);
  }

	public List<CrmRecordId> findByOwnerAndCrmSourceAndCrmRecordType(SocialOrganization owner, CrmSystemType source, CrmRecordType type) {
    return repository.findByOwnerAndCrmSourceAndCrmRecordType(owner, source, type);
  }

	public CrmRecordId findByLocalIdAndCrmSourceAndCrmRecordType(Long localId, CrmSystemType source, CrmRecordType type) {
    return repository.findByLocalIdAndCrmSourceAndCrmRecordType( localId, source, type);
  }

	public CrmRecordId findByRemoteIdAndCrmSourceAndCrmRecordType(String remoteId, CrmSystemType source, CrmRecordType type) {
    return repository.findByRemoteIdAndCrmSourceAndCrmRecordType(remoteId, source, type);
  }

	public void delete(Long id) {
    repository.delete(id);
  }
	
	public CrmRecordId create() {
		CrmRecordId crmRecordId = new CrmRecordId();
		return crmRecordId;
	}
}
