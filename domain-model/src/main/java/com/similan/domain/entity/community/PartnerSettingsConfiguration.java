package com.similan.domain.entity.community;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "PartnerSettingsConfiguration")
public class PartnerSettingsConfiguration implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private String inCommunitySearchUrl;
	
	@Column
	private String embeddedCommunitySearchUrl;
	
	@ManyToOne
	private SocialOrganization organization;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public SocialOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(SocialOrganization organization) {
		this.organization = organization;
	}

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
