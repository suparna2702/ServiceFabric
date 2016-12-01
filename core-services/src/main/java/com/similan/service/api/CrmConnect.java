package com.similan.service.api;

import com.similan.domain.entity.lead.CrmLeadSettings;
import com.similan.domain.entity.lead.CrmLeadSyncRecord;
import com.similan.framework.dto.lead.CrmLeadDto;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.service.exception.CoreServiceException;

public interface CrmConnect {
	public abstract CrmLeadSyncRecord performLeadSync(
			CrmLeadSettings crmLeadSettings) throws CoreServiceException, Exception;

	public abstract CrmLeadDto getRemoteLeadDtoForLead(CrmLeadSettings crmLeadSettings, LeadDto leadDto);
}
