package com.similan.domain.entity.metadata;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "countryTypeList")
public class CountryTypeList {
	
	private List<CountryType> countryList;

	@XmlElements(@XmlElement(name="country",type=CountryType.class))
	public List<CountryType> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<CountryType> countryList) {
		this.countryList = countryList;
	}
}
