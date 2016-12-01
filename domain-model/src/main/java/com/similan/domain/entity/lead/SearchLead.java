package com.similan.domain.entity.lead;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity(name = "SearchLead")
@DiscriminatorValue("SearchLead")
public class SearchLead extends Lead {
   
    @Embedded
    private SearchLeadDetail searchDetail;

	public SearchLeadDetail getSearchDetail() {
		return searchDetail;
	}
	
	public void setSearchDetail(SearchLeadDetail searchDetail) {
		this.searchDetail = searchDetail;
	}
}
