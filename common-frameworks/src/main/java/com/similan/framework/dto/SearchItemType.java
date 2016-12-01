package com.similan.framework.dto;

import java.io.Serializable;
import java.util.List;

public class SearchItemType implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> selectedSearchType;

	public SearchItemType() {
	}

	public List<String> getSelectedSearchType() {
		return selectedSearchType;
	}

	public void setSelectedSearchType(List<String> selectedSearchType) {
		this.selectedSearchType = selectedSearchType;
	}

}
