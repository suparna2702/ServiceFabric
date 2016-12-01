package com.similan.adminapp.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import com.similan.framework.dto.lead.AffiliateLeadListDto;

@ViewScoped
@ManagedBean(name = "affiliateLeadListView")
@Slf4j
public class AffiliateLeadListView extends BaseAdminView {

	private static final long serialVersionUID = 1L;
	
	private List<AffiliateLeadListDto> affiliateLeadList;
	
	@PostConstruct
	public void init() {
		
		try {
			this.affiliateLeadList = this.getAffiliateLeadService()
                    .getAffiliateLeadListForBusiness(null);
			
		}
		catch(Exception exp){
           log.error("Error initializing view ", exp);	
           FacesContext.getCurrentInstance().addMessage(null, 
                   new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
                   "Failed to fetch lead list " + exp.getMessage())); 
		}
	}

	public List<AffiliateLeadListDto> getAffiliateLeadList() {
		return affiliateLeadList;
	}

	public void setAffiliateLeadList(List<AffiliateLeadListDto> affiliateLeadList) {
		this.affiliateLeadList = affiliateLeadList;
	}
}
