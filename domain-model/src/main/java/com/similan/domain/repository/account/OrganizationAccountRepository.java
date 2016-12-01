package com.similan.domain.repository.account;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.acccount.AccountRecordState;
import com.similan.domain.entity.acccount.AccountRecordType;
import com.similan.domain.entity.acccount.LeadPurchaseRecord;
import com.similan.domain.entity.acccount.OrganizationAccount;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.lead.AcquiredLead;
import com.similan.domain.entity.lead.AffiliateLead;
import com.similan.domain.repository.account.jpa.JpaOrganizationAccountRepository;

@Repository
public class OrganizationAccountRepository {
	
	@Autowired
	private JpaOrganizationAccountRepository repository;
	
	public  OrganizationAccount findOne(Long id) {
	  return repository.findOne(id);
	}
	
	public OrganizationAccount save(OrganizationAccount account) {
	  return repository.save(account);
	}
	
	public List<OrganizationAccount> findAll() {
	  return repository.findAll();
	}
	
	public OrganizationAccount findByOrg(SocialOrganization org) {
	  return repository.findByOrg(org);
	}
	
	public LeadPurchaseRecord createLeadPurchaseRecord(AffiliateLead affLead, AcquiredLead acqLead){
		LeadPurchaseRecord leadRecord = new LeadPurchaseRecord();
		leadRecord.setPurchasedFrom(affLead);
		leadRecord.setPurchasedLead(acqLead);
		leadRecord.setTimeStamp(new Date());
		leadRecord.setRecordType(AccountRecordType.LeadPurchase);
		leadRecord.setAccountRecordState(AccountRecordState.NotProcessed);
		
		return leadRecord;
	}
	
	public OrganizationAccount create(){
		OrganizationAccount account = new OrganizationAccount();
		return account;
	}

}
