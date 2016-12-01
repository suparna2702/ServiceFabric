package com.similan.framework.dto.update;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.similan.domain.entity.community.SuggestionInfo;

@XmlRootElement(name = "suggestionInfoDto")
public class SuggestionInfoDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private SuggestionInfo suggestionInfo;
	
	public SuggestionInfo getSuggestionInfo() {
		return suggestionInfo;
	}

	public void setSuggestionInfo(SuggestionInfo suggestionInfo) {
		this.suggestionInfo = suggestionInfo;
	}

	@XmlTransient
	public String getSuggestionIcon() {
		return suggestionInfo.getSuggestionIcon();
	}

	public void setSuggestionIcon(String suggestionIcon) {
		this.suggestionInfo.setSuggestionIcon(suggestionIcon);
	}

	@XmlTransient
	public String getSuggestionLink() {
		return this.suggestionInfo.getSuggestionLink();
	}

	public void setSuggestionLink(String suggestionLink) {
		this.suggestionInfo.setSuggestionLink(suggestionLink);
	}
	
	@XmlTransient
	public String getSuggestionLinkUrl() {
		return this.suggestionInfo.getSuggestionLinkUrl();
	}

	public void setSuggestionLinkUrl(String suggestionLinkUrl) {
		this.suggestionInfo.setSuggestionLinkUrl(suggestionLinkUrl);
	}

	@XmlTransient
	public String getSuggestionHeader() {
		return this.suggestionInfo.getSuggestionHeader();
	}

	public void setSuggestionHeader(String suggestionHeader) {
		this.suggestionInfo.setSuggestionHeader(suggestionHeader);
	}

	@XmlTransient
	public String getContent() {
		return this.suggestionInfo.getContent();
	}

	public void setContent(String content) {
		this.suggestionInfo.setContent(content);
	}
}
