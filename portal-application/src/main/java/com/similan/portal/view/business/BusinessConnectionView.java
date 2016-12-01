package com.similan.portal.view.business;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.portal.view.BaseView;

@Scope("request")
@Component("businessConnectionView")
@Slf4j
public class BusinessConnectionView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	private OrganizationBasicInfoDto businessToConnect;
	private String processInstanceId = StringUtils.EMPTY;
	Boolean apprvStatus = Boolean.FALSE;
	
	@PostConstruct
	public void init() {
		
		String orgId = this.getContextParam("oid");
		processInstanceId = this.getContextParam("pid");
		String statusParam = this.getContextParam("status");
		apprvStatus = Boolean.valueOf(statusParam);
		
    	log.info("Organization id " + orgId + " process instance " + processInstanceId + " status " + apprvStatus);
    	businessToConnect = this.getOrgService().getBasicOrganizationInfo(Long.valueOf(orgId));
    	
		if(apprvStatus != true){
			this.rejectConnection();
		}
		else {
			this.approveConnection();
		}
		
    }

	public OrganizationBasicInfoDto getBusinessToConnect() {
		return businessToConnect;
	}

	public void setBusinessToConnect(OrganizationBasicInfoDto businessToConnect) {
		this.businessToConnect = businessToConnect;
	}
	
	public void approveConnection(){
		log.info("Approving business connection");
		this.orgService.approveConnection(businessToConnect, processInstanceId, true);
	}
	
	public void rejectConnection(){
		log.info("Rejecting business connection");
		this.orgService.approveConnection(businessToConnect, processInstanceId, true);
	}
	
	

}
