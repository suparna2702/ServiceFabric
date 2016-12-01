package com.similan.service.api;

import com.similan.domain.entity.lead.CrmLeadSettings;

public interface CrmConnectFactory {
	public abstract CrmConnect getCrmConnect(CrmLeadSettings crmLeadSettings);
}
