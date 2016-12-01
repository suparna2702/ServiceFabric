package com.similan.adminapp.dto;

import java.io.Serializable;

public class TemplateInfo implements Serializable {
	
	private static final long serialVersionUID = 9023559360384704367L;

	private String id;
	
	private String name;
	
	private String templateText;
	
	private String property;
	
	public String getTemplateText() {
		return templateText;
	}

	public void setTemplateText(String templateText) {
		this.templateText = templateText;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}
