package com.similan.portal.view.leadportal;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.leadcapture.LeadCaptureWizzardDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("leadCaptureContactView")
@Slf4j
public class LeadCaptureContactView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	private LeadCaptureWizzardDto captureWizzard;
	
	private OrganizationDetailInfoDto orgInfo;
	
	private String orgEmbeddedIdentity = StringUtils.EMPTY;
	
	@PostConstruct
	public void init(){
		
		try {
			
			this.orgEmbeddedIdentity = getContextParam("oeid");
			log.info("embedded org id " + this.orgEmbeddedIdentity);
			Long orgId = this.orgService.getOrgIdFromEmbeddedId(orgEmbeddedIdentity);
			orgInfo = this.getOrgService()
					      .getOrganization(orgId);
			
		}
		catch(Exception exp){
			log.error("Error ", exp);
		}
		
		log.info("Initializing the view LeadCaptureContactView ");
		captureWizzard = this.getOrgService()
				             .getLeadCaptureWizzard(orgInfo);
		
	}
	
	public String getOrgEmbeddedIdentity() {
		return orgEmbeddedIdentity;
	}

	public void setOrgEmbeddedIdentity(String orgEmbeddedIdentity) {
		this.orgEmbeddedIdentity = orgEmbeddedIdentity;
	}

	public LeadCaptureWizzardDto getCaptureWizzard() {
		return captureWizzard;
	}

	public void setCaptureWizzard(LeadCaptureWizzardDto captureWizzard) {
		this.captureWizzard = captureWizzard;
	}
	
	public void contact(){
		log.info("Contacting org " + this.captureWizzard);
		this.getOrgService().saveLeadCaptureresponse(captureWizzard, orgInfo);
	}

}
