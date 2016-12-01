package com.similan.framework.dto.search;

import java.io.Serializable;

public class SearchStatisticsDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long searched;
	
	private Long viewed;
	
	private Integer periodInDays;

	public Long getSearched() {
		return searched;
	}

	public void setSearched(Long searched) {
		this.searched = searched;
	}

	public Long getViewed() {
		return viewed;
	}

	public void setViewed(Long viewed) {
		this.viewed = viewed;
	}

	public Integer getPeriodInDays() {
		return periodInDays;
	}

	public void setPeriodInDays(Integer periodInDays) {
		this.periodInDays = periodInDays;
	}

}
