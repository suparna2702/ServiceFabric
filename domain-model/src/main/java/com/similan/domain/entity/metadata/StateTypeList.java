package com.similan.domain.entity.metadata;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "stateTypeList")
public class StateTypeList {
	
	private List<StateType> stateTypeList;

	@XmlElements(@XmlElement(name="state",type=StateType.class))
	public List<StateType> getStateTypeList() {
		return stateTypeList;
	}

	public void setStateTypeList(List<StateType> stateTypeList) {
		this.stateTypeList = stateTypeList;
	}
}
