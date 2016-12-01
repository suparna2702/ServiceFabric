package com.similan.portal.view.leadportal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.lead.CrmLeadSettingsDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("crmLeadSettingView")
@Slf4j
public class CrmLeadFeedSettingView extends BaseView {
  private static final long serialVersionUID = 5601330032484099950L;

	
	private CrmLeadSettingsDto crmLeadSettings;
	
	private DualListModel<String> leadExportFieldList;
	
	private DualListModel<String> leadImportFieldList;
	
	@Autowired
	private OrganizationDetailInfoDto orgInfo = null;
	
	@PostConstruct
	public void init() {
		
		try {
			//this.crmLeadSettings = this.getLeadService().getCrmSettings(orgInfo);
			
		}
		catch(Exception exp){
			log.error("Cannot get CRM settings ", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure to get CRM settings", 
                   "Failure to get CRM settings"));
		}
		
		if(this.crmLeadSettings == null){
			this.crmLeadSettings = new CrmLeadSettingsDto();
		}
		
		/* populate the pick list model for export fields */
		List<String> srcExportFieldList = new ArrayList<String>();
		List<String> destExportFieldList = new ArrayList<String>();
		
		//List<String> leadFieldMetaList = this.getLeadService().getCrmLeadExportImportFields();
		List<String> domainExportLeadFieldList = this.crmLeadSettings.getLeadFieldExports();
		
		/* populate the dest list */
		for(String leadField : domainExportLeadFieldList){
			destExportFieldList.add(leadField);
		}
		
		/* populate the source list */
		/*for(String leadField : leadFieldMetaList){
			
			if(containsIgnoreCase(destExportFieldList, leadField) == false){
				srcExportFieldList.add(leadField);
			}
		}
		*/
		this.leadExportFieldList = new DualListModel<String>(srcExportFieldList, destExportFieldList);
		
		/* populate the import fields */
		List<String> srcImportFieldList = new ArrayList<String>();
		List<String> destImportFieldList = new ArrayList<String>();
		
		List<String> domainImportLeadFieldList = this.crmLeadSettings.getLeadFieldImports();
		
		/* populate the dest */
		for(String leadField : domainImportLeadFieldList){
			destImportFieldList.add(leadField);
		}
		
		/* populate the source list 
        for(String leadField : leadFieldMetaList){
			
			if(containsIgnoreCase(destImportFieldList, leadField) == false){
				srcImportFieldList.add(leadField);
			}
		}*/
		
		this.leadImportFieldList = new DualListModel<String>(srcImportFieldList, destImportFieldList);
    }
	
	public DualListModel<String> getLeadImportFieldList() {
		return leadImportFieldList;
	}

	public void setLeadImportFieldList(DualListModel<String> leadImportFieldList) {
		this.leadImportFieldList = leadImportFieldList;
	}

	public DualListModel<String> getLeadExportFieldList() {
		return leadExportFieldList;
	}

	public void setLeadExportFieldList(DualListModel<String> leadExportFieldList) {
		this.leadExportFieldList = leadExportFieldList;
	}

	public OrganizationDetailInfoDto getOrgInfo() {
		return orgInfo;
	}

	public void setOrgInfo(OrganizationDetailInfoDto orgInfo) {
		this.orgInfo = orgInfo;
	}

	public CrmLeadSettingsDto getCrmLeadSettings() {
		return crmLeadSettings;
	}

	public void setCrmLeadSettings(CrmLeadSettingsDto crmLeadSettings) {
		this.crmLeadSettings = crmLeadSettings;
	}

	public void save(){
		log.info("Saving CRM settings " + crmLeadSettings.toString());
		
		try {
			this.crmLeadSettings.setLeadFieldImports(leadImportFieldList.getTarget());
			this.crmLeadSettings.setLeadFieldExports(leadExportFieldList.getTarget());
			
			//this.getLeadService().saveCrmSettings(this.orgInfo, this.crmLeadSettings);
			FacesContext.getCurrentInstance().addMessage(null, 
	                   new FacesMessage(FacesMessage.SEVERITY_INFO,"CRM Settings successfully saved", 
	                   "CRM Settings successfully saved"));  
		}
		catch(Exception exp){
			log.error("Cannot save CRM settings ", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure to save CRM settings", 
                   "Failure to save CRM settings"));
		}
	}

}
