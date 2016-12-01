package com.similan.domain.entity.community;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity(name = "OrganizationSuggestion")
@DiscriminatorValue("OrganizationSuggestion")
public class OrganizationSuggestion extends Suggestion {
	
	@ManyToOne
	private SocialOrganization suggestionForOrg;
	
	@Column
	private String industry;

	public SocialOrganization getSuggestionForOrg() {
		return suggestionForOrg;
	}

	public void setSuggestionForOrg(SocialOrganization suggestionForOrg) {
		this.suggestionForOrg = suggestionForOrg;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

}
