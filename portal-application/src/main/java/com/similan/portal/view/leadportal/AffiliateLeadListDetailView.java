package com.similan.portal.view.leadportal;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import com.similan.framework.dto.lead.AffiliateLeadListDto;
import com.similan.framework.util.JsfUtils;
import com.similan.portal.view.BaseView;

@ViewScoped
@ManagedBean(name = "affiliateLeadListDetailView")
@Slf4j
public class AffiliateLeadListDetailView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	private AffiliateLeadListDto leadList = null; 
	
	@PostConstruct
	public void init() {
		String leadListId = JsfUtils.getContextParam("lid");
		log.info("Initializng AffiliateLeadListDetailView for " + leadListId);
		
		if(leadListId != null){
			Long listId = Long.valueOf(leadListId);
			try {
				leadList = this.affiliateLeadService.getAffiliateLeadListDetail(listId);
				FacesContext.getCurrentInstance().addMessage(null, 
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", 
	                    "Successfully fetched lead list details")); 
			}
			catch(Exception exp){
				log.error("Failed to fetch lead list ", exp);
				FacesContext.getCurrentInstance().addMessage(null, 
		                   new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
		                   "Failed to fetch lead list " + exp.getMessage())); 
			}
			
		}
	}

	public AffiliateLeadListDto getLeadList() {
		return leadList;
	}

	public void setLeadList(AffiliateLeadListDto leadList) {
		this.leadList = leadList;
	}

}
