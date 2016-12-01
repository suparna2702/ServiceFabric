package com.similan.adminapp.view;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.event.FileUploadEvent;

import com.similan.framework.dto.lead.AffiliateLeadListDto;

@ViewScoped
@ManagedBean(name = "affiliateLeadUploadView")
@Slf4j
public class AffiliateLeadUploadView extends BaseAdminView {

	private static final long serialVersionUID = 1L;
	
	private AffiliateLeadListDto leadUploadList = null; 
	
    private Boolean leadContactVerification;
	
	private Boolean leadIntentVerification;
	
	@PostConstruct
	public void init() {
		
		leadUploadList = new AffiliateLeadListDto();
		this.leadContactVerification = Boolean.TRUE;
		this.leadIntentVerification = Boolean.TRUE;
		
		log.info("Post init in AffiliateLeadUploadView");
	}

	public AffiliateLeadListDto getLeadUploadList() {
		return leadUploadList;
	}

	public void setLeadUploadList(AffiliateLeadListDto leadUploadList) {
		this.leadUploadList = leadUploadList;
	}
	
	public Boolean getLeadContactVerification() {
		return leadContactVerification;
	}

	public void setLeadContactVerification(Boolean leadContactVerification) {
		this.leadContactVerification = leadContactVerification;
	}

	public Boolean getLeadIntentVerification() {
		return leadIntentVerification;
	}

	public void setLeadIntentVerification(Boolean leadIntentVerification) {
		this.leadIntentVerification = leadIntentVerification;
	}

	public void handleFileUpload(FileUploadEvent event) {
		
		if(event == null){
			log.warn("Received null file upload event");
			return;
		}
		
		log.info("Uploaded file name " + event.getFile().getFileName());
		
		try {
			// todo
		}
		catch (Exception bie) {
			log.error("Error while importing beans " + bie.getMessage());
			facesHelper.addDirectMessage(null, FacesMessage.SEVERITY_ERROR, 
					              bie.getMessage());
		}
		
	}
	
	public void uploadLeads(){
		log.info("Uploading affiliate leads ");
		try {
			this.affiliateLeadService.saveAffiliateLeadList(leadUploadList);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", 
                    "Successfully uploaded leads")); 
			facesHelper.redirect("/adminui/lead/affiliateLeadList.xhtml");			
		}
		catch(Exception exp){
			log.error("Cannot upload affiliate leads ", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
	                   new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
	                   "Cannot upload leads " + exp.getMessage()));  
		}
	}

}
