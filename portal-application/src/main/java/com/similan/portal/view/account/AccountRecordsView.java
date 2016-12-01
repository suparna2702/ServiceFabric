package com.similan.portal.view.account;

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.account.OrganizationAccountRecordDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("accountRecordsView")
@Slf4j
public class AccountRecordsView extends BaseView {
  
	private static final long serialVersionUID = -1L;
	
	@Autowired
	private OrganizationDetailInfoDto orgInfo;
	
	private List<OrganizationAccountRecordDto> orgAccountRecords;
	
	@PostConstruct
	public void init() {
		
		try {
			orgAccountRecords = this.orgService.getAccountRecords(orgInfo);
		}
		catch(Exception exp){
			log.info("Cannot fetch account records ", exp);
		}
    	
    }

	public List<OrganizationAccountRecordDto> getOrgAccountRecords() {
		return orgAccountRecords;
	}

	public void setOrgAccountRecords(
			List<OrganizationAccountRecordDto> orAccountRecords) {
		this.orgAccountRecords = orAccountRecords;
	}

}
