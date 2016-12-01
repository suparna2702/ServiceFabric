package com.similan.domain.entity.metadata;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "stateFile")
public class StateFileMapping {
	
	private String countryName;
	
	private String stateFileName;

	@XmlAttribute(name = "countryName")
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@XmlAttribute(name = "stateFileName")
	public String getStateFileName() {
		return stateFileName;
	}

	public void setStateFileName(String stateFileName) {
		this.stateFileName = stateFileName;
	}
}
