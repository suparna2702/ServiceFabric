package com.similan.framework.dto.lead;

import java.io.Serializable;

import com.similan.domain.common.YesNoEnum;
import com.similan.domain.entity.lead.LeadAlertSettings;

public class LeadAlertSettingsDto implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private Long id;
	
    private Boolean all;
	
    private Boolean newLeadArrival;
    
    private Boolean newLeadTransfer;
    
    private Boolean memberLeadUpdate;
    
    private Boolean alertViaEmail;
    
    private Boolean alertViaSms;
    
    public LeadAlertSettingsDto(){
    	this.id = Long.MIN_VALUE;
    	this.all = Boolean.TRUE;
    	this.newLeadArrival = Boolean.TRUE;
    	this.newLeadTransfer = Boolean.TRUE;
    	this.memberLeadUpdate = Boolean.TRUE;
    	this.alertViaEmail = Boolean.TRUE;
    	this.alertViaSms = Boolean.TRUE;
    }
    
    public void updateFromDomainObject(LeadAlertSettings alertSettings){
    	if(alertSettings != null){
    		
    		if(alertSettings.getAllValues().equals(YesNoEnum.Yes) == true){
    			this.all = true;
    		}
    		else {
    			this.all = false;
    		}
    		
    		if(alertSettings.getNewLeadArrival().equals(YesNoEnum.Yes) == true){
    			this.newLeadArrival = true;
    		}
    		else {
    			this.newLeadArrival = false;
    		}
    		
    		if(alertSettings.getNewLeadTransfer().equals(YesNoEnum.Yes) == true){
    			this.newLeadTransfer = true;
    		}
    		else {
    			this.newLeadTransfer = false;
    		}
    		
    		if(alertSettings.getMemberLeadUpdate().equals(YesNoEnum.Yes) == true){
    			this.memberLeadUpdate = true;
    		}
    		else {
    			this.memberLeadUpdate = false;
    		}
    		
    		if(alertSettings.getAlertViaEmail().equals(YesNoEnum.Yes) == true){
    			this.alertViaEmail = true;
    		}
    		else {
    			this.alertViaEmail = false;
    		}
    		
    		if(alertSettings.getAlertViaSms().equals(YesNoEnum.Yes) == true){
    			this.alertViaSms = true;
    		}
    		else {
    			this.alertViaSms = false;
    		}
    		
    		this.id = alertSettings.getId();
    		
    	}
    }
    
    public void updateDomainObject(LeadAlertSettings alertSettings){
    	
    	if(alertSettings != null){
    		
    		if(this.all ==  true){
    			alertSettings.setAllValues(YesNoEnum.Yes);
    		}
    		else {
    			alertSettings.setAllValues(YesNoEnum.No);
    		}
    		
    		if(this.newLeadArrival ==  true){
    			alertSettings.setNewLeadArrival(YesNoEnum.Yes);
    		}
    		else {
    			alertSettings.setNewLeadArrival(YesNoEnum.No);
    		}
    		
    		if(this.newLeadTransfer ==  true){
    			alertSettings.setNewLeadTransfer(YesNoEnum.Yes);
    		}
    		else {
    			alertSettings.setNewLeadTransfer(YesNoEnum.No);
    		}
    		
    		if(this.memberLeadUpdate ==  true){
    			alertSettings.setMemberLeadUpdate(YesNoEnum.Yes);
    		}
    		else {
    			alertSettings.setMemberLeadUpdate(YesNoEnum.No);
    		}
    		
    		if(this.alertViaEmail ==  true){
    			alertSettings.setAlertViaEmail(YesNoEnum.Yes);
    		}
    		else {
    			alertSettings.setAlertViaEmail(YesNoEnum.No);
    		}
    		
    		if(this.alertViaSms ==  true){
    			alertSettings.setAlertViaSms(YesNoEnum.Yes);
    		}
    		else {
    			alertSettings.setAlertViaSms(YesNoEnum.No);
    		}
    	}
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getAll() {
		return all;
	}

	public void setAll(Boolean all) {
		this.all = all;
	}

	public Boolean getNewLeadArrival() {
		return newLeadArrival;
	}

	public void setNewLeadArrival(Boolean newLeadArrival) {
		this.newLeadArrival = newLeadArrival;
	}

	public Boolean getNewLeadTransfer() {
		return newLeadTransfer;
	}

	public void setNewLeadTransfer(Boolean newLeadTransfer) {
		this.newLeadTransfer = newLeadTransfer;
	}

	public Boolean getMemberLeadUpdate() {
		return memberLeadUpdate;
	}

	public void setMemberLeadUpdate(Boolean memberLeadUpdate) {
		this.memberLeadUpdate = memberLeadUpdate;
	}

	public Boolean getAlertViaEmail() {
		return alertViaEmail;
	}

	public void setAlertViaEmail(Boolean alertViaEmail) {
		this.alertViaEmail = alertViaEmail;
	}

	public Boolean getAlertViaSms() {
		return alertViaSms;
	}

	public void setAlertViaSms(Boolean alertViaSms) {
		this.alertViaSms = alertViaSms;
	}
 
	@Override
	public String toString() {
		return "LeadAlertSettingsDto [id=" + id + ", all=" + all
				+ ", newLeadArrival=" + newLeadArrival + ", newLeadTransfer="
				+ newLeadTransfer + ", memberLeadUpdate=" + memberLeadUpdate
				+ ", alertViaEmail=" + alertViaEmail + ", alertViaSms="
				+ alertViaSms + "]";
	}
}
