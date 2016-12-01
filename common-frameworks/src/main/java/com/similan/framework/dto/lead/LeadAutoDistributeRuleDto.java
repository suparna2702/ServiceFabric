package com.similan.framework.dto.lead;

import java.io.Serializable;
import java.util.List;

public class LeadAutoDistributeRuleDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private Long id;
	
    private String name;
	
    private String street;
	
	private String country;
	
	private String state;
	
	private String city;
	
	private String zipCode;
	
	private Integer limitPerDay;
	
	private Integer distanceRange;
	
	private List<String> keywords;
	
	private List<String> companies;
	
	private List<String> industry;
	
	private LeadDto owner;

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

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Integer getLimitPerDay() {
		return limitPerDay;
	}

	public void setLimitPerDay(Integer limitPerDay) {
		this.limitPerDay = limitPerDay;
	}

	public Integer getDistanceRange() {
		return distanceRange;
	}

	public void setDistanceRange(Integer distanceRange) {
		this.distanceRange = distanceRange;
	}

	public LeadDto getOwner() {
		return owner;
	}

	public void setOwner(LeadDto owner) {
		this.owner = owner;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public List<String> getCompanies() {
		return companies;
	}

	public void setCompanies(List<String> companies) {
		this.companies = companies;
	}

	public List<String> getIndustry() {
		return industry;
	}

	public void setIndustry(List<String> industry) {
		this.industry = industry;
	}
	
}
