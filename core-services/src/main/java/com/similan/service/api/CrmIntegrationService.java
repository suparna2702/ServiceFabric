package com.similan.service.api;

import com.similan.framework.dto.lead.CrmLeadDto;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.service.api.community.dto.basic.SocialActorContactDto;

public interface CrmIntegrationService {

  public void startLeadSync(SocialActorContactDto contact) throws Exception;

  public CrmLeadDto getRemoteLeadDtoForLead(LeadDto leadDto);

  public void synchronizeAll();

  public boolean isSynchronizationInProcess(SocialActorContactDto contact);
}
