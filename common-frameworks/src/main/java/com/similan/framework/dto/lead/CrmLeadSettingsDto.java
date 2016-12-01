package com.similan.framework.dto.lead;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.similan.domain.entity.lead.CrmLeadFieldSettingConfig;
import com.similan.domain.entity.lead.CrmSystemType;

public class CrmLeadSettingsDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private CrmSystemType crmSystemType;
	
	private Map<String, String> parameters;
	
	private Boolean enableTransferToCrm;
	
	private Boolean enableTransferFromCrm;
	
	private Boolean enableQueryFromCrm;
	
	private Boolean notificationEmailToOrg;
	
	private List<String> leadFieldExports;
	
	private List<String> leadFieldImports;
	
	public CrmLeadSettingsDto(){
		
		this.id = Long.MIN_VALUE;
		this.enableTransferToCrm = Boolean.TRUE;
		this.enableTransferFromCrm = Boolean.TRUE;
		this.enableQueryFromCrm = Boolean.TRUE;
		this.notificationEmailToOrg = Boolean.TRUE;
		
		this.parameters = new HashMap<String, String>();
		this.leadFieldExports = new ArrayList<String>();
		this.leadFieldImports = new ArrayList<String>();
		
	}
	
	public Boolean getNotificationEmailToOrg() {
		return notificationEmailToOrg;
	}

	public void setNotificationEmailToOrg(Boolean notificationEmailToOrg) {
		this.notificationEmailToOrg = notificationEmailToOrg;
	}

	public List<String> getLeadFieldImports() {
		return leadFieldImports;
	}

	public void setLeadFieldImports(List<String> leadFieldImports) {
		this.leadFieldImports = leadFieldImports;
	}

	public List<String> getLeadFieldExports() {
		return leadFieldExports;
	}

	public void setLeadFieldExports(List<String> leadFieldExports) {
		this.leadFieldExports = leadFieldExports;
	}

	public Long getId() {
		return id;
	}
	
	public Boolean getEnableQueryFromCrm() {
		return enableQueryFromCrm;
	}

	public void setEnableQueryFromCrm(Boolean enableQueryFromCrm) {
		this.enableQueryFromCrm = enableQueryFromCrm;
	}

	public CrmSystemType getCrmSystemType() {
		return crmSystemType;
	}

	public void setCrmSystemType(CrmSystemType crmSystemType) {
		this.crmSystemType = crmSystemType;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Boolean getEnableTransferToCrm() {
		return enableTransferToCrm;
	}

	public void setEnableTransferToCrm(Boolean enableTransferToCrm) {
		this.enableTransferToCrm = enableTransferToCrm;
	}

	public Boolean getEnableTransferFromCrm() {
		return enableTransferFromCrm;
	}

	public void setEnableTransferFromCrm(Boolean enableTransferFromCrm) {
		this.enableTransferFromCrm = enableTransferFromCrm;
	}

	
	public void setCrmFieldSettingConfig(CrmLeadFieldSettingConfig config){
		
		if(config != null){
			
			for(String exportField : config.getLeadFieldExports()){
				this.leadFieldExports.add(exportField);
			}
			
			for(String importField : config.getLeadFieldImports()){
				this.leadFieldImports.add(importField);
			}
		}
	}

	public CrmLeadFieldSettingConfig getCrmFieldSettingConfig() {
		
		CrmLeadFieldSettingConfig crmLeadFieldSettingConfig = new CrmLeadFieldSettingConfig();
		if(this.leadFieldExports != null){
			for(String exportFields : this.leadFieldExports){
				crmLeadFieldSettingConfig.getLeadFieldExports().add(exportFields);
			}
		}
		
		if(this.leadFieldImports != null){
			for(String importField : this.leadFieldImports){
				crmLeadFieldSettingConfig.getLeadFieldImports().add(importField);
			}
		}
		
		return crmLeadFieldSettingConfig;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
}
