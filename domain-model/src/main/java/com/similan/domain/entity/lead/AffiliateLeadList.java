package com.similan.domain.entity.lead;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name="AffiliateLeadList")
public class AffiliateLeadList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
	private Long id;
	
	@Column
	private Long affiliateId;
	
	@Column
	private String name;
	
	@Column
	private String sourceDescription;
	
	@Column
	private Date ingestionTime;
	
	@Column
	private Float defaultPrice = Float.MIN_NORMAL; 
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<AffiliateLead> ingestedLeads;
	
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

	public List<AffiliateLead> getIngestedLeads() {
		return ingestedLeads;
	}

	public void setIngestedLeads(List<AffiliateLead> ingestedLeads) {
		this.ingestedLeads = ingestedLeads;
	}

	public Long getAffiliateId() {
		return affiliateId;
	}

	public void setAffiliateId(Long affiliateId) {
		this.affiliateId = affiliateId;
	}
	

}
