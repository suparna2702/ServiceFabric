package com.similan.domain.entity.community;

public enum OrganizationType {
	UNSPECIFIED(""),
	
	RESELLER("Reseller"),

	SUPPLIER("Supplier"),

	DISTRIBUTOR("Distributor"),
	
	LEADAFFILIATE("Lead Affiliate"), 
	
	AGENT("Agent"), 
	
	TECHNOLOGY_PARTNER("Technology Partner"), 
	
	MARKETING_PARTNER("Marketing Partner"),

	;
	
	private String keywords;

  private OrganizationType(String keywords) {
	  this.keywords = keywords;
	}
  
  public String getKeywords() {
    return keywords;
  }
}