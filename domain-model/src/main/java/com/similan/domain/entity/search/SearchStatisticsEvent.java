package com.similan.domain.entity.search;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "SearchStatisticsEvent")
public class SearchStatisticsEvent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Embedded
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
