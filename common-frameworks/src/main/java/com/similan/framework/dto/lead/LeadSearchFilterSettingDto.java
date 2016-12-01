package com.similan.framework.dto.lead;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.similan.domain.entity.lead.LeadSearchFilterSettingConfig;

public class LeadSearchFilterSettingDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private String frequency;
	
	private Date activeSince;
	
	private Long lastResultCount;
	
    private String street;
	
	private String country;
	
	private String state;
	
	private String city;
	
	private String zipCode;
	
	private String radius;
	
	private String industry;
	
	private String distanceUnitOption;
	
	private List<String> selectedSearchType;
	
	private List<LeadSearchFilterKeywordDto> keywordFilters;
	
	private List<String> titleList;
	
	private List<String> industryList;
	
	public LeadSearchFilterSettingDto(){
		
		this.selectedSearchType = new ArrayList<String>();
		this.keywordFilters = new ArrayList<LeadSearchFilterKeywordDto>();
		this.titleList = new ArrayList<String>();
		this.industryList = new ArrayList<String>();
		
		this.country = "United States";
		this.state = "Alabama";
		this.id = Long.MIN_VALUE;
	}
	
	public void updateListSettingsFromDomain(
			LeadSearchFilterSettingConfig settings) {
		
		/* title */
		if(settings.getTitles() != null){
			for(String title : settings.getTitles()){
				this.titleList.add(title);
			}
		}
		
		/* keywords */
		if(settings.getKeywords() != null){
			for(String keyword : settings.getKeywords()){
				LeadSearchFilterKeywordDto keywordDto = new LeadSearchFilterKeywordDto();
				keywordDto.setKeyword(keyword);
				this.keywordFilters.add(keywordDto);
			}
		}
		
		/* industry */
		if(settings.getIndustries() != null){
			for(String industry : settings.getIndustries()){
				this.industryList.add(industry);
			}
		}
		
	}
	
	public LeadSearchFilterSettingConfig getSearchFilterSettingConfig(){
		LeadSearchFilterSettingConfig settingConfig = new LeadSearchFilterSettingConfig();
		
		/* all titles */
		if(this.getTitleList() != null){
			for(String title : this.getTitleList()){
				settingConfig.getTitles().add(title);
			}
		}
		
		/* keywords */
		if(this.getKeywordFilters() != null){
			for(LeadSearchFilterKeywordDto keyword : this.getKeywordFilters()){
				settingConfig.getKeywords().add(keyword.getKeyword());
			}
		}
		
		/* Industry */
		if(this.getIndustryList() != null){
			for(String industry : this.getIndustryList()){
				settingConfig.getIndustries().add(industry);
			}
		}
		
		return settingConfig;
	}
	
	public List<String> getIndustryList() {
		return industryList;
	}

	public void setIndustryList(List<String> industryList) {
		this.industryList = industryList;
	}

	public List<String> getTitleList() {
		return titleList;
	}

	public void setTitleList(List<String> titleList) {
		this.titleList = titleList;
	}

	public List<LeadSearchFilterKeywordDto> getKeywordFilters() {
		return keywordFilters;
	}

	public void setKeywordFilters(List<LeadSearchFilterKeywordDto> keywordFilters) {
		this.keywordFilters = keywordFilters;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

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

	public Date getActiveSince() {
		return activeSince;
	}

	public void setActiveSince(Date activeSince) {
		this.activeSince = activeSince;
	}
	
	public String getActiveSinceDate() {
		return activeSince.toString();
	}

	public void setActiveSinceDate(String activeSince) {
		/* do nothing */
	}

	public Long getLastResultCount() {
		if(lastResultCount == null)
			return 0l;
		return lastResultCount;
	}

	public void setLastResultCount(Long results) {
		this.lastResultCount = results;
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

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getDistanceUnitOption() {
		return distanceUnitOption;
	}

	public void setDistanceUnitOption(String distanceUnitOption) {
		this.distanceUnitOption = distanceUnitOption;
	}

	public List<String> getSelectedSearchType() {
		return selectedSearchType;
	}

	public void setSelectedSearchType(List<String> selectedSearchType) {
		this.selectedSearchType = selectedSearchType;
	}
	
	public void setAllKeywordFilters(String str){
		/* do nothing */
	}
	
	public String getAllKeywordFilters(){
		StringBuilder keywordBuilder = new StringBuilder();
		for(LeadSearchFilterKeywordDto searchFilter : keywordFilters){
			keywordBuilder.append(searchFilter.getKeyword()).append(", ");
		}
		
		return keywordBuilder.toString();
	}
	
	public void setAllIndustryFilters(String str){
		/* do nothing */
	}
	
	public String getAllIndustryFilters(){
		StringBuilder industryBuilder = new StringBuilder();
		for(String industry : industryList){
			industryBuilder.append(industry).append(", ");
		}
		
		return industryBuilder.toString();
	}


	@Override
	public String toString() {
		return "LeadSearchFilterSettingDto [id=" + id + ", name=" + name
				+ ", frequency=" + frequency + ", activeSince=" + activeSince
				+ ", results=" + lastResultCount + ", street=" + street + ", country="
				+ country + ", state=" + state + ", city=" + city
				+ ", zipCode=" + zipCode + ", radius=" + radius + ", industry="
				+ industry + ", distanceUnitOption=" + distanceUnitOption
				+ ", selectedSearchType=" + selectedSearchType + "]";
	}

}
