package com.similan.framework.manager.management;

import org.codehaus.jackson.type.JavaType;

import com.similan.framework.Mutable;

public class ConfigurationParameterReference<T> implements Mutable<T> {

	private ConfigurationManagerImpl configurationService;

	private JavaType type;

	private String beanName;

	private String propertyName;

	private boolean used = false;

	private boolean removed = false;

	private T value;

	public ConfigurationParameterReference(
			ConfigurationManagerImpl configurationService, JavaType type,
			String beanName, String propertyName) {
		this.configurationService = configurationService;
		this.type = type;
		this.beanName = beanName;
		this.propertyName = propertyName;
	}

	public T getValue() {
		return value;
	}

	public void doSetValue(T value) {
		this.value = value;
	}

	public void setValue(T value) {
		configurationService.setValue(type, beanName, propertyName, value);
	}

	public boolean isUsed() {
		return used;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public JavaType getType() {
		return type;
	}
}

