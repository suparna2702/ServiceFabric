package com.similan.framework.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "searchResult")
public class SearchResultDto implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private SearchResultSummery resultSummary;
	
	private List<SearchResultItemDto> searchResults = new ArrayList<SearchResultItemDto>();
	
	private Long fromSocialActorId;
	
	@XmlElements(@XmlElement(name="searchResultItem", type=com.similan.framework.dto.SearchResultItemDto.class))
	public List<SearchResultItemDto> getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(List<SearchResultItemDto> searchResults) {
		this.searchResults = searchResults;
	}

	@XmlElement(name="searchResultSummary", type=com.similan.framework.dto.SearchResultSummery.class)
	public SearchResultSummery getResultSummary() {
		return resultSummary;
	}

	public void setResultSummary(SearchResultSummery resultSummary) {
		this.resultSummary = resultSummary;
	}

	public Long getFromSocialActorId() {
		return fromSocialActorId;
	}

	public void setFromSocialActorId(Long fromSocialActorId) {
		this.fromSocialActorId = fromSocialActorId;
	}
	
}
