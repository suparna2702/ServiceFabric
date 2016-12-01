package com.similan.framework.manager.management;

import java.io.Serializable;

public class ConfigurationParameterDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String beanName;
	private String propertyName;
	private String type;
	private String value;

	protected ConfigurationParameterDto() {
	}

	public ConfigurationParameterDto(String beanName, String propertyName,
			String type, String value) {
		this.beanName = beanName;
		this.propertyName = propertyName;
		this.type = type;
		this.value = value;
	}

	public String getBeanName() {
		return beanName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

}
