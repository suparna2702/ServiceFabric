package com.similan.framework.dto.metadata;

import java.io.Serializable;

public class IndustryTypeDto implements Serializable {

	private static final long serialVersionUID = -8564736306638460929L;
	
	Long id;
	String industryName;
	
	public IndustryTypeDto() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIndustryName() {
		return industryName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}


}