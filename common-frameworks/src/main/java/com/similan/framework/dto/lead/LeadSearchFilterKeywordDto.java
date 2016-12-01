package com.similan.framework.dto.lead;

import java.io.Serializable;

public class LeadSearchFilterKeywordDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String keyword;
	
	public LeadSearchFilterKeywordDto(){
		this.id = Long.MIN_VALUE;
		this.keyword = "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
