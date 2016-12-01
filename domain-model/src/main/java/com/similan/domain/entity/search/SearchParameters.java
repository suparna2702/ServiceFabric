package com.similan.domain.entity.search;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.similan.domain.repository.global.GlobalRepository.SearchTargetType;

public class SearchParameters implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String street;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private String zipCode;
	
	private String searchString;
	
	private String searchFilters;
	
	private Long partnerActorId;
	
	private Long searcherActorId;
	
	private Long searchResultActorId;
	
	private Date timeStamp;
	
	@Column(length=1000)
	private String sessionId;
	
	private Long totalSearchResults;
	
	private Long searchRank;
	
	@Column
	@Enumerated(EnumType.STRING)
	private SearchTargetType searchTargetType;
	
	@Column
	@Enumerated(EnumType.STRING)
	private SearchEventType searchEventType;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getSearchFilters() {
		return searchFilters;
	}

	public void setSearchFilters(String searchFilters) {
		this.searchFilters = searchFilters;
	}

	public Long getPartnerActorId() {
		return partnerActorId;
	}

	public void setPartnerActorId(Long partnerActorId) {
		this.partnerActorId = partnerActorId;
	}

	public Long getSearcherActorId() {
		return searcherActorId;
	}

	public void setSearcherActorId(Long searcherActorId) {
		this.searcherActorId = searcherActorId;
	}

	public Long getSearchResultActorId() {
		return searchResultActorId;
	}

	public void setSearchResultActorId(Long searchResultActorId) {
		this.searchResultActorId = searchResultActorId;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Long getTotalSearchResults() {
		return totalSearchResults;
	}

	public void setTotalSearchResults(Long totalSearchResults) {
		this.totalSearchResults = totalSearchResults;
	}

	public Long getSearchRank() {
		return searchRank;
	}

	public void setSearchRank(Long searchRank) {
		this.searchRank = searchRank;
	}

	public SearchTargetType getSearchTargetType() {
		return searchTargetType;
	}

	public void setSearchTargetType(SearchTargetType searchTargetType) {
		this.searchTargetType = searchTargetType;
	}

	public SearchEventType getSearchEventType() {
		return searchEventType;
	}

	public void setSearchEventType(SearchEventType searchEventType) {
		this.searchEventType = searchEventType;
	}
}
