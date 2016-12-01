package com.similan.domain.entity.partner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name="PartnerSearchSettingsConfiguration")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"orgId"})})
public class PartnerSearchSettingsConfiguration {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private Long orgId;
	
	@Column(length=5000)
	private String configurationSettings;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getConfigurationSettings() {
		return configurationSettings;
	}

	public void setConfigurationSettings(String configurationSettings) {
		this.configurationSettings = configurationSettings;
	}

}
