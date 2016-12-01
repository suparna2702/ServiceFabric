package com.similan.framework.dto.partner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "partnerSearchSettingsConfiguration")
public class PartnerSearchSettingsConfigurationDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long orgId;
	
	private Long id;
	
    private int inCommunitySearchLimitConfig;
	
	private int embeddedSearchLimitConfig;
	
	private List<String> embeddedSearchDisplayConfig;
	
	private List<String> inCommunitySearchDisplayConfig;
	
	private boolean embeddedSearchConfigEnabled;
	
	private boolean embeddedSearchConfigShowMap;
	
	private boolean inCommunitySearchConfigEnabled;
	
	private boolean inCommunitySearchConfigShowMap;
	
	public PartnerSearchSettingsConfigurationDto(){
		embeddedSearchDisplayConfig = new ArrayList<String>();
		inCommunitySearchDisplayConfig = new ArrayList<String>();
		this.id = Long.MIN_VALUE;
	}
	
	@XmlTransient
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlTransient
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	@XmlAttribute(name = "inCommunitySearchLimitConfig")
	public int getInCommunitySearchLimitConfig() {
		return inCommunitySearchLimitConfig;
	}

	public void setInCommunitySearchLimitConfig(int inCommunitySearchLimitConfig) {
		this.inCommunitySearchLimitConfig = inCommunitySearchLimitConfig;
	}

	@XmlAttribute(name = "embeddedSearchLimitConfig")
	public int getEmbeddedSearchLimitConfig() {
		return embeddedSearchLimitConfig;
	}

	public void setEmbeddedSearchLimitConfig(int embeddedSearchLimitConfig) {
		this.embeddedSearchLimitConfig = embeddedSearchLimitConfig;
	}

	@XmlElements(@XmlElement(name="embeddedSearchDisplayConfig"))
	public List<String> getEmbeddedSearchDisplayConfig() {
		return embeddedSearchDisplayConfig;
	}

	public void setEmbeddedSearchDisplayConfig(
			List<String> embeddedSearchDisplayConfig) {
		this.embeddedSearchDisplayConfig = embeddedSearchDisplayConfig;
	}

	@XmlElements(@XmlElement(name="inCommunitySearchDisplayConfig"))
	public List<String> getInCommunitySearchDisplayConfig() {
		return inCommunitySearchDisplayConfig;
	}

	public void setInCommunitySearchDisplayConfig(
			List<String> inCommunitySearchDisplayConfig) {
		this.inCommunitySearchDisplayConfig = inCommunitySearchDisplayConfig;
	}

	@XmlAttribute(name = "embeddedSearchConfigEnabled")
	public boolean getEmbeddedSearchConfigEnabled() {
		return embeddedSearchConfigEnabled;
	}

	public void setEmbeddedSearchConfigEnabled(boolean embeddedSearchConfigEnabled) {
		this.embeddedSearchConfigEnabled = embeddedSearchConfigEnabled;
	}

	@XmlAttribute(name = "embeddedSearchConfigShowMap")
	public boolean getEmbeddedSearchConfigShowMap() {
		return embeddedSearchConfigShowMap;
	}

	public void setEmbeddedSearchConfigShowMap(boolean embeddedSearchConfigShowMap) {
		this.embeddedSearchConfigShowMap = embeddedSearchConfigShowMap;
	}

	@XmlAttribute(name = "inCommunitySearchConfigEnabled")
	public boolean getInCommunitySearchConfigEnabled() {
		return inCommunitySearchConfigEnabled;
	}

	public void setInCommunitySearchConfigEnabled(
			boolean inCommunitySearchConfigEnabled) {
		this.inCommunitySearchConfigEnabled = inCommunitySearchConfigEnabled;
	}

	@XmlAttribute(name = "inCommunitySearchConfigShowMap")
	public boolean getInCommunitySearchConfigShowMap() {
		return inCommunitySearchConfigShowMap;
	}

	public void setInCommunitySearchConfigShowMap(
			boolean inCommunitySearchConfigShowMap) {
		this.inCommunitySearchConfigShowMap = inCommunitySearchConfigShowMap;
	}

}
