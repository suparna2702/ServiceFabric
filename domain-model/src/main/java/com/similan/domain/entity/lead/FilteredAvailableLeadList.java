package com.similan.domain.entity.lead;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity(name = "FilteredAvailableLeadList")
public class FilteredAvailableLeadList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "Filtered_List_ID", unique = true, nullable = false)
	private Long id;
	
	@Column
	private Long forSocialActorId;
	
	@ManyToOne
	private LeadSearchFilterSetting filterUsed;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	private Set<AffiliateLead> filteredLeads;
	
	@Column
	private Date timeStamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getForSocialActorId() {
		return forSocialActorId;
	}

	public void setForSocialActorId(Long forSocialActorId) {
		this.forSocialActorId = forSocialActorId;
	}

	public LeadSearchFilterSetting getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(LeadSearchFilterSetting filterUsed) {
		this.filterUsed = filterUsed;
	}

	public Set<AffiliateLead> getFilteredLeads() {
		return filteredLeads;
	}

	public void setFilteredLeads(Set<AffiliateLead> filteredLeads) {
		this.filteredLeads = filteredLeads;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	

}
