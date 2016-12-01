package com.similan.framework.dto;

import java.io.Serializable;

public class ServiceInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	
	private String serviceType;
	
	private String serviceDescription;

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}
	
}
