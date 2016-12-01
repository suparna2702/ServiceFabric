package com.similan.service.impl;

import com.similan.domain.entity.lead.CrmLeadSettings;
import com.similan.domain.entity.lead.CrmSystemType;
import com.similan.service.api.CrmConnect;
import com.similan.service.api.CrmConnectFactory;

public class CrmConnectFactoryImpl implements CrmConnectFactory {
	
	private CrmZohoConnect crmZohoConnect;

	public CrmConnect getCrmConnect(CrmLeadSettings crmLeadSettings) {
		if (crmLeadSettings.getCrmSystemType() == CrmSystemType.Zoho) {
			return crmZohoConnect;
		}
		return null;
	}

	public CrmZohoConnect getCrmZohoConnect() {
		return crmZohoConnect;
	}

	public void setCrmZohoConnect(CrmZohoConnect crmZohoConnect) {
		this.crmZohoConnect = crmZohoConnect;
	}
}
