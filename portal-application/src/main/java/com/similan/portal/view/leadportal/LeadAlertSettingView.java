package com.similan.portal.view.leadportal;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.lead.LeadAlertSettingsDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("leadAlertSettingView")
@Slf4j
public class LeadAlertSettingView extends BaseView {
  private static final long serialVersionUID = 1L;

	
    private LeadAlertSettingsDto leadAlertSettings;
    
    @Autowired(required = false)
	private MemberInfoDto member = null;
	
	@PostConstruct
    public void init() {
		
		/*this.leadAlertSettings = this.getLeadService().getLeadAlertSettings(member);
		if(this.leadAlertSettings == null){
			this.leadAlertSettings = new LeadAlertSettingsDto();
		}*/
	}
	
	public LeadAlertSettingsDto getLeadAlertSettings() {
		return leadAlertSettings;
	}

	public void setLeadAlertSettings(LeadAlertSettingsDto leadAlertSettings) {
		this.leadAlertSettings = leadAlertSettings;
	}

	public MemberInfoDto getMember() {
		return member;
	}

	public void setMember(MemberInfoDto member) {
		this.member = member;
	}

	public void save(){
		/*log.info("Saving alert settings ");
		try {
			this.getLeadService().saveLeadAlertSettings(this.getMember(), 
                    this.getLeadAlertSettings());
			FacesContext.getCurrentInstance().addMessage(null, 
	                   new FacesMessage(FacesMessage.SEVERITY_INFO,"Lead Alert Save", 
	                   "Lead Alert setting Saved successfully"));  
			
		}
		catch(Exception exp){
			log.info("Error saving lead alert settings ", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Lead Alert Save", 
                   "Failure to save Lead Alert"));  
		}*/
	}

	
}
