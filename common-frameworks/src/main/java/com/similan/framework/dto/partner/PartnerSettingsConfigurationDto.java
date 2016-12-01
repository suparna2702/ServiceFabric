package com.similan.framework.dto.partner;

import java.io.Serializable;

public class PartnerSettingsConfigurationDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String inCommunitySearchUrl;
	
	private String embeddedCommunitySearchUrl;

	public String getInCommunitySearchUrl() {
		return inCommunitySearchUrl;
	}

	public void setInCommunitySearchUrl(String inCommunitySearchUrl) {
		this.inCommunitySearchUrl = inCommunitySearchUrl;
	}

	public String getEmbeddedCommunitySearchUrl() {
		return embeddedCommunitySearchUrl;
	}

	public void setEmbeddedCommunitySearchUrl(String embeddedCommunitySearchUrl) {
		this.embeddedCommunitySearchUrl = embeddedCommunitySearchUrl;
	}
}
