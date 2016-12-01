package com.similan.domain.entity.metadata;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "industryTypeList")
public class IndustryTypeList {
	
	private List<IndustryType> industryList;

	@XmlElements(@XmlElement(name="industry",type=IndustryType.class))
	public List<IndustryType> getIndustryList() {
		return industryList;
	}

	public void setIndustryList(List<IndustryType> industryList) {
		this.industryList = industryList;
	}
}
