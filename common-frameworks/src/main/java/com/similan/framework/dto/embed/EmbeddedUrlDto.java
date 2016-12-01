package com.similan.framework.dto.embed;

import java.io.Serializable;

public class EmbeddedUrlDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String urlTag;
  
  private String fullUrl;
  
  private String helpUrl;

	public String getUrlTag() {
		return urlTag;
	}

	public void setUrlTag(String urlTag) {
		this.urlTag = urlTag;
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}
	
	public String getHelpUrl() {
    return helpUrl;
  }
	
	public void setHelpUrl(String helpUrl) {
    this.helpUrl = helpUrl;
  }

	@Override
	public String toString() {
		return "EmbeddedUrlDto [urlTag=" + urlTag + ", fullUrl=" + fullUrl
				+ "]";
	}

    
}
