package com.similan.framework.dto.search;

import java.io.Serializable;

import com.similan.domain.entity.search.SearchParameters;

public class SearchStatisticsEventDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private SearchParameters searchParams;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SearchParameters getSearchParams() {
		return searchParams;
	}

	public void setSearchParams(SearchParameters searchParams) {
		this.searchParams = searchParams;
	}

}
