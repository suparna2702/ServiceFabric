package com.similan.portal.view.partner;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.partner.PartnerSearchSettingsConfigurationDto;
import com.similan.framework.dto.partner.PartnerSettingsConfigurationDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("partnerConfigSettingView")
@Slf4j
public class PartnerConfigSettingsView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	private PartnerSettingsConfigurationDto partnerSettings;

	@Autowired
	private MemberInfoDto memberInfo;

	@Autowired
	private OrganizationDetailInfoDto orgInfo;
	
	private PartnerSearchSettingsConfigurationDto partnerSearchSettings = null;
	
	@PostConstruct
	public void init() {
		
		this.partnerSettings = this.orgService.getPartnerSettings(memberInfo, orgInfo);
		
		/**/
		try {
			this.partnerSearchSettings = this.getOrgService().getPartnerSearchSettings(orgInfo.getId());
			if(this.partnerSearchSettings == null){
				this.partnerSearchSettings = new PartnerSearchSettingsConfigurationDto();
				this.partnerSearchSettings.setOrgId(orgInfo.getId());
			}
		}
		catch(Exception exp){
			log.error("Cannot get partner serach config");
		}
    	
    }
	
	public PartnerSearchSettingsConfigurationDto getPartnerSearchSettings() {
		return partnerSearchSettings;
	}

	public void setPartnerSearchSettings(
			PartnerSearchSettingsConfigurationDto partnerSearchSettings) {
		this.partnerSearchSettings = partnerSearchSettings;
	}

	public void updatePartnerSearchSettings(){
		log.info("Updating partner search settings " + partnerSearchSettings.toString());
		try {
			this.getOrgService().updatePartnerConfiguration(this.partnerSearchSettings);
		}
		catch(Exception exp){
			log.error("Cannot update partner search settings ", exp);
		}
	}

	public PartnerSettingsConfigurationDto getPartnerSettings() {
		return partnerSettings;
	}

	public void setPartnerSettings(PartnerSettingsConfigurationDto partnerSettings) {
		this.partnerSettings = partnerSettings;
	}

	public void generateSearchUrl(){
		
		log.info("Regenrating Generating URL");
	}
}
