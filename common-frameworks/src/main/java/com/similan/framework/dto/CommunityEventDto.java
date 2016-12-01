package com.similan.framework.dto;

import java.io.Serializable;
import java.util.Date;

import com.similan.domain.entity.event.LoginInfo;

public class CommunityEventDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private SearchResultDto searchResult;
	
	private LoginInfo loginInfo;
	
	private Long eventGeneratorId;
	
	private Date eventGenerationTime;
	
	private CommunityEventType eventType;
	
	private long sessionDuration;
	
	public long getSessionDuration() {
		return sessionDuration;
	}

	public void setSessionDuration(long sessionDuration) {
		this.sessionDuration = sessionDuration;
	}

	public LoginInfo getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}

	public CommunityEventType getEventType() {
		return eventType;
	}

	public void setEventType(CommunityEventType eventType) {
		this.eventType = eventType;
	}

	public Long getEventGeneratorId() {
		return eventGeneratorId;
	}

	public void setEventGeneratorId(Long eventGeneratorId) {
		this.eventGeneratorId = eventGeneratorId;
	}

	public Date getEventGenerationTime() {
		return eventGenerationTime;
	}

	public void setEventGenerationTime(Date eventGenerationTime) {
		this.eventGenerationTime = eventGenerationTime;
	}

	public SearchResultDto getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(SearchResultDto searchResult) {
		this.searchResult = searchResult;
	}
}
