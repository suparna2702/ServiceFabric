package com.similan.framework.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

public class AdvancedSearchInput implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String street;
	
	private String country;
	
	private String state;
	
	private String city;
	
	private String zipCode;
	
	private Double radius;
	
	private String industry;
	
	private String keyWord;
	
	private String distanceUnitOption;
	
	private List<String> selectedSearchType;
	
	public AdvancedSearchInput(){
		
		this.selectedSearchType = new ArrayList<String>();
		this.country = "United States";
		this.state = "Alabama";
	}
	
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}



	public List<String> getSelectedSearchType() {
		if(selectedSearchType == null){
			selectedSearchType = new ArrayList<String>();
		}
		
		return selectedSearchType;
	}

	public void setSelectedSearchType(List<String> selectedSearchType) {
		this.selectedSearchType = selectedSearchType;
	}

	public String getDistanceUnitOption() {
		return distanceUnitOption;
	}

	public void setDistanceUnitOption(String distanceUnitOption) {
		this.distanceUnitOption = distanceUnitOption;
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

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}
	
	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String toString(){
		
		StringBuilder retString = new StringBuilder();
		retString.append("Street : ").append(this.street)
		.append("City : ").append(this.city)
		.append(" State : ").append(this.state)
		.append(" Country : ").append(this.country)
		.append(" Zip Code : ").append(this.zipCode)
		.append(" Radius : ").append(this.radius)
		.append(" Keyword : ").append(this.keyWord);
		
		return retString.toString();
	}

	public void setSelectedSearchType(String type) {
		if (StringUtils.isBlank(type)) {
			return;
		}
		type = type.replace('[', ' ').replace(']', ' ').trim();
		this.selectedSearchType = new ArrayList<String>(Arrays.asList(type
				.split(",")));
	}
}
