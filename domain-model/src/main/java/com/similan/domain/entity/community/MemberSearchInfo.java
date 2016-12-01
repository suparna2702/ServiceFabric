package com.similan.domain.entity.community;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "MemberSearchInfo")
public class MemberSearchInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private String searchSource;
	
	@Column
	private long whoSearched;
	
	@Column
	private long memberSearched;
	
	@Column
	Date timeSearched;
	
	@Column
	String searchString;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSearchSource() {
		return searchSource;
	}

	public void setSearchSource(String searchSource) {
		this.searchSource = searchSource;
	}

	public long getWhoSearched() {
		return whoSearched;
	}

	public void setWhoSearched(long whoSearched) {
		this.whoSearched = whoSearched;
	}

	public long getMemberSearched() {
		return memberSearched;
	}

	public void setMemberSearched(long memberSearched) {
		this.memberSearched = memberSearched;
	}

	public Date getTimeSearched() {
		return timeSearched;
	}

	public void setTimeSearched(Date timeSearched) {
		this.timeSearched = timeSearched;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
}
