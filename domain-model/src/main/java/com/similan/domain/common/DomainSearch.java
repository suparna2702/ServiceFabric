package com.similan.domain.common;

import java.util.List;

public class DomainSearch {
	
	private String searchString;
	
	private List<DomainSearchFilter> exclusionList;

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public List<DomainSearchFilter> getExclusionList() {
		return exclusionList;
	}

	public void setExclusionList(List<DomainSearchFilter> exclusionList) {
		this.exclusionList = exclusionList;
	}
}
