package com.similan.domain.entity.lead;

import java.io.Serializable;

public class SearchLeadDetail implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String searchString;
	
	private Long totalRecordsFound;
	
	private Integer rank;

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public Long getTotalRecordsFound() {
		return totalRecordsFound;
	}

	public void setTotalRecordsFound(Long totalRecordsFound) {
		this.totalRecordsFound = totalRecordsFound;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
}
