package com.similan.framework.dto.lead;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AffiliateLeadListDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long affiliateId;
	
	private String name;
	
	private String sourceDescription;
	
	private Date ingestionTime;
	
	private Float defaultPrice = Float.valueOf("1.0");
	
	private List<LeadDto> leadList;
	
	public AffiliateLeadListDto(){
		leadList = new ArrayList<LeadDto>();
		defaultPrice = Float.valueOf("1.0");
	}
	
	public Float getDefaultPrice() {
		return defaultPrice;
	}

	public void setDefaultPrice(Float defaultPrice) {
		this.defaultPrice = defaultPrice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAffiliateId() {
		return affiliateId;
	}

	public void setAffiliateId(Long affiliateId) {
		this.affiliateId = affiliateId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSourceDescription() {
		return sourceDescription;
	}

	public void setSourceDescription(String sourceDescription) {
		this.sourceDescription = sourceDescription;
	}

	public Date getIngestionTime() {
		return ingestionTime;
	}

	public void setIngestionTime(Date ingestionTime) {
		this.ingestionTime = ingestionTime;
	}

	public List<LeadDto> getLeadList() {
		return leadList;
	}

	public void setLeadList(List<LeadDto> leadList) {
		this.leadList = leadList;
	}

	@Override
	public String toString() {
		return "AffiliateLeadListDto [id=" + id + ", affiliateId="
				+ affiliateId + ", name=" + name + ", sourceDescription="
				+ sourceDescription + ", ingestionTime=" + ingestionTime
				+ ", leadList=" + leadList + "]";
	}
	
}
