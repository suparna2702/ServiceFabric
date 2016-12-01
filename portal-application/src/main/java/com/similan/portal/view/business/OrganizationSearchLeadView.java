package com.similan.portal.view.business;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.dto.lead.LeadSearchFilterSettingDto;
import com.similan.framework.util.JsfUtils;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("organizationSearchLeadView")
@Slf4j
public class OrganizationSearchLeadView extends BaseView {
	
	private static final long serialVersionUID = 1L;
	
	protected List<LeadDto> availableLeads = null;
	
	protected LeadSearchFilterSettingDto leadSearchFilterSettingDto;
	
	@Autowired
	private OrganizationDetailInfoDto orgInfo;

	public List<LeadDto> getAvailableLeads() {
		return availableLeads;
	}

	public void setAvailableLeads(List<LeadDto> availableLeads) {
		this.availableLeads = availableLeads;
	}
	
	public LeadSearchFilterSettingDto getLeadSearchFilterSettingDto() {
		return leadSearchFilterSettingDto;
	}

	public void setLeadSearchFilterSettingDto(
			LeadSearchFilterSettingDto leadSearchFilterSettingDto) {
		this.leadSearchFilterSettingDto = leadSearchFilterSettingDto;
	}

	@PostConstruct
	public void init() {		
		/* 1. get the search filter id from the context 
		 * 2. Then get all the affiliate leads for that 
		 *    search filter 
		 *    */
		String searchFilterId = JsfUtils.getContextParam("fid");
		
		if(searchFilterId != null){
			Long filterId = Long.valueOf(searchFilterId);
			log.info("Fetching affiliate leads by search filter id " + searchFilterId);
			
			try {
				this.availableLeads = this.getAffiliateLeadService()
		                  .getAffiliateLeadsByFilter(filterId);

				/* get the lead search filter settings too 
				this.leadSearchFilterSettingDto = this.getLeadService()
						                              .getLeadSearchFilterSettingsById(filterId);*/
				
			}
			catch(Exception exp){
				log.error("Cannot initialize OrganizationSearchLeadView ", exp);
			}
			
		}
	}
	
	public String purchaseAvailableLead(LeadDto affLead){
		
		log.info("Purchasing lead id " + affLead);
		
		try {
			this.getAffiliateLeadService().purchaseAffilateLead(orgInfo, affLead);
			
			for(LeadDto leadDto : availableLeads){
				if(leadDto.getId() == affLead.getId()){
					availableLeads.remove(leadDto);
					break;
				}
			}
			
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", 
                    "Thanks for purchasing lead"));
		}
		catch(Exception exp){
			log.error("Failed to purchase lead", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
                    "Failed to purchase lead "));
		}
		
		return "result";
	}
}
