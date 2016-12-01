package com.similan.portal.view.leadportal;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.importexport.csv.BeanImportingException;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("orgLeadUploadView")
@Slf4j
public class MemberLeadUploadView extends BaseView {
	
	private static final long serialVersionUID = -5340310986321575551L;
	
	private List<LeadDto> leadList;
	
	private Boolean leadContactVerification;
	
	private Boolean leadIntentVerification;
	
	private String leadSource;
	
	@Autowired
	private MemberInfoDto memberInfo;
	
	@Autowired
	private OrganizationDetailInfoDto orgInfo;
	
	@PostConstruct
	public void init() {
		this.leadContactVerification = Boolean.TRUE;
		this.leadIntentVerification = Boolean.TRUE;
    }
	
	public String getLeadSource() {
		return leadSource;
	}

	public void setLeadSource(String leadSource) {
		this.leadSource = leadSource;
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

	public List<LeadDto> getLeadList() {
		return leadList;
	}

	public void setLeadList(List<LeadDto> leadList) {
		this.leadList = leadList;
	}

	public void handleFileUpload(FileUploadEvent event) {
		
		/*if(event == null){
			log.warn("Received null file upload event");
			return;
		}
		
		log.info("Uploaded file name " + event.getFile().getFileName());
		
		try {
			
			if(StringUtils.isEmpty(leadSource)){
				leadSource = "Bulk upload by " + this.memberInfo.getFullName() 
						                  + " for " + this.orgInfo.getBusinessName();
			}
			leadList = this.leadService.handleLeadFileUpload(event, leadSource);
			if(leadList != null){
				facesHelper.addDirectMessage(null, FacesMessage.SEVERITY_INFO, 
						"Successfully imported leads : " + leadList.size());
				
			}
			else {
				facesHelper.addDirectMessage(null, FacesMessage.SEVERITY_WARN, 
						"No leads found to import");
				
			}
		}
		catch (BeanImportingException bie) {
			log.error("Error while importing beans ",bie);
			facesHelper.addDirectMessage(null, FacesMessage.SEVERITY_ERROR, 
					"Error while importing leads " + bie.getMessage());
		}
		catch(Exception exp){
			log.error("Error while importing beans ", exp);
			facesHelper.addDirectMessage(null, FacesMessage.SEVERITY_ERROR, 
					"Error while importing leads " + exp.getMessage());
		} */
		
	}
	
	public String uploadLead(){ 
	/*
		log.info("Uploaded leads " + leadList.size() 
				  + " upload option " + leadContactVerification);
		try {
			this.getLeadService().saveImportedLeads(leadList, orgInfo);
			FacesContext.getCurrentInstance().addMessage(null, 
	                   new FacesMessage(FacesMessage.SEVERITY_INFO,"Lead Upload Status", 
	                   "Successfully uploaded leads"));  	
		}
		catch(Exception exp){
			log.error("Error importing acquired leads ", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Lead Import Status", 
                    "Failed to import leads"));
		}*/
		
		return "result";
	}

}
