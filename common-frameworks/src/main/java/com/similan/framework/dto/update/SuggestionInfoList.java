package com.similan.framework.dto.update;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "suggestionInfoList")
public class SuggestionInfoList implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<SuggestionInfoDto> suggestionList;

	@XmlElements(@XmlElement(name="searchResultItem", type=com.similan.framework.dto.update.SuggestionInfoDto.class))
	public List<SuggestionInfoDto> getSuggestionList() {
		return suggestionList;
	}

	public void setSuggestionList(List<SuggestionInfoDto> suggestionList) {
		this.suggestionList = suggestionList;
	}
}
