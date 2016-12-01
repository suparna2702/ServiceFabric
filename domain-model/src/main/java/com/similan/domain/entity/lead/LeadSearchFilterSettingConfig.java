package com.similan.domain.entity.lead;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "leadSearchFilterSettingConfig")
public class LeadSearchFilterSettingConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<String> titles;
	
	private List<String> keywords;
	
	private List<String> industries;
	
	public LeadSearchFilterSettingConfig(){
		titles = new ArrayList<String>();
		keywords = new ArrayList<String>();
		industries = new ArrayList<String>();
	}

	@XmlElements(@XmlElement(name="titles"))
	public List<String> getTitles() {
		return titles;
	}

	public void setTitles(List<String> titles) {
		this.titles = titles;
	}
	
	@XmlElements(@XmlElement(name="keywords"))
	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	@XmlElements(@XmlElement(name="industries"))
	public List<String> getIndustries() {
		return industries;
	}

	public void setIndustries(List<String> industries) {
		this.industries = industries;
	}
}
