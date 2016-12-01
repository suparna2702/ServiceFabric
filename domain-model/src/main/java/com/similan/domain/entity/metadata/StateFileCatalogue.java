package com.similan.domain.entity.metadata;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "stateFileCatalogue")
public class StateFileCatalogue {
	
	private List<StateFileMapping> stateFileList;

	@XmlElements(@XmlElement(name="stateFile",type=StateFileMapping.class))
	public List<StateFileMapping> getStateFileList() {
		return stateFileList;
	}

	public void setStateFileList(List<StateFileMapping> stateFileList) {
		this.stateFileList = stateFileList;
	}
	
	public StateFileMapping getStateMappingByCountry(String countryName){
		StateFileMapping retStateMapping = null;
		
		for(StateFileMapping stateMapping : stateFileList){
			if(stateMapping.getCountryName().equalsIgnoreCase(countryName) == true){
				retStateMapping = stateMapping;
			}
		}
		
		return retStateMapping;
	}
}
