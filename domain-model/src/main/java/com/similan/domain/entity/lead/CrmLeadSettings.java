package com.similan.domain.entity.lead;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;

import com.similan.domain.entity.community.SocialOrganization;

@Entity(name = "CrmLeadSettings")
public class CrmLeadSettings {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
	private Long id;

	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean enableTransferToCrm;

	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean enableTransferFromCrm;

	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean enableQueryFromCrm;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean notificationEmailToOrg;

	@Column(length = 10000)
	private String configListItems;

	@Enumerated(EnumType.STRING)
	@Column
	private CrmSystemType crmSystemType;
	
	@ElementCollection
	@MapKeyColumn(name = "parameterName")
	@Column(name = "parameterValue")
	@CollectionTable(name = "CrmLeadSettingsParameter")
	Map<String, String> parameters = new HashMap<String, String>();

	@ManyToOne
	private SocialOrganization owner;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Boolean getNotificationEmailToOrg() {
		return notificationEmailToOrg;
	}

	public void setNotificationEmailToOrg(Boolean notificationEmailToOrg) {
		this.notificationEmailToOrg = notificationEmailToOrg;
	}

	public SocialOrganization getOwner() {
		return owner;
	}

	public void setOwner(SocialOrganization owner) {
		this.owner = owner;
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

	public Boolean getEnableQueryFromCrm() {
		return enableQueryFromCrm;
	}

	public void setEnableQueryFromCrm(Boolean enableQueryFromCrm) {
		this.enableQueryFromCrm = enableQueryFromCrm;
	}

	public String getConfigListItems() {
		return configListItems;
	}

	public void setConfigListItems(String configListItems) {
		this.configListItems = configListItems;
	}

	public CrmSystemType getCrmSystemType() {
		return crmSystemType;
	}

	public void setCrmSystemType(CrmSystemType crmSystemType) {
		this.crmSystemType = crmSystemType;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

}
