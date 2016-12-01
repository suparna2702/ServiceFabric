package com.similan.framework.dto.update;

import java.util.UUID;

public class SystemUpdateEventDto extends WallEventDto {

	private static final long serialVersionUID = 1L;
	
    private String header;
    
    private String categoryTag;
	
	private String updateLink;
	
	private String updateLinkText;
	
	private String content;
	
	private String industry;
	
	private String updateIcon = null;
	
	private String uuID = UUID.randomUUID().toString();
	
	public String getUuID() {
		return uuID;
	}

	public void setUuID(String uuID) {
		this.uuID = uuID;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getCategoryTag() {
		return categoryTag;
	}

	public void setCategoryTag(String categoryTag) {
		this.categoryTag = categoryTag;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getUpdateLink() {
		return updateLink;
	}

	public void setUpdateLink(String updateLink) {
		this.updateLink = updateLink;
	}

	public String getUpdateLinkText() {
		return updateLinkText;
	}

	public void setUpdateLinkText(String updateLinkText) {
		this.updateLinkText = updateLinkText;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUpdateIcon() {
		return updateIcon;
	}

	public void setUpdateIcon(String updateIcon) {
		this.updateIcon = updateIcon;
	}
	
}
