package com.similan.domain.entity.community;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "suggestionInfo")
public class SuggestionInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long suggestionFor;
	
	private String suggestionIcon;
	
	private String content;
	
	private String suggestionLink;
	
	private String suggestionLinkUrl;
	
	private String suggestionHeader;
	
	@Enumerated(EnumType.ORDINAL)
	@Column
	private SuggestionSourceType suggestionType;
	
	@Column
	private Date postDate;
	
	@Column
	private Integer validForDays;
	
	public Integer getValidForDays() {
		return validForDays;
	}

	public void setValidForDays(Integer validForDays) {
		this.validForDays = validForDays;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public String getSuggestionLinkUrl() {
		return suggestionLinkUrl;
	}

	public void setSuggestionLinkUrl(String suggestionLinkUrl) {
		this.suggestionLinkUrl = suggestionLinkUrl;
	}

	public Long getSuggestionFor() {
		return suggestionFor;
	}

	public void setSuggestionFor(Long suggestionFor) {
		this.suggestionFor = suggestionFor;
	}

	public SuggestionSourceType getSuggestionType() {
		return suggestionType;
	}

	public void setSuggestionType(SuggestionSourceType suggestionType) {
		this.suggestionType = suggestionType;
	}

	public String getSuggestionIcon() {
		return suggestionIcon;
	}

	public void setSuggestionIcon(String suggestionIcon) {
		this.suggestionIcon = suggestionIcon;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSuggestionLink() {
		return suggestionLink;
	}

	public void setSuggestionLink(String suggestionLink) {
		this.suggestionLink = suggestionLink;
	}

	public String getSuggestionHeader() {
		return suggestionHeader;
	}

	public void setSuggestionHeader(String suggestionHeader) {
		this.suggestionHeader = suggestionHeader;
	}
}
