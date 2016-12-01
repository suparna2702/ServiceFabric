package com.similan.service.api;

import java.util.List;

import com.similan.framework.dto.lead.CrmLeadActivityDto;
import com.similan.framework.dto.lead.CrmLeadDto;
import com.similan.framework.dto.lead.CrmLeadNoteDto;
import com.similan.service.exception.CoreServiceException;

public interface ZohoFacade {

	public abstract List<CrmLeadDto> getZohoLeads(String authToken) throws CoreServiceException;

	public abstract List<CrmLeadDto> getZohoLeads(String authToken,
			long start, long end)
			throws CoreServiceException;

	public abstract void updateLead(String authToken, CrmLeadDto lead)
			throws CoreServiceException;

	public abstract void createLead(String authToken, CrmLeadDto lead)
			throws CoreServiceException;

	public abstract CrmLeadDto getLead(String authToken, String crmId)
			throws CoreServiceException;

	public abstract void createLeadNote(String zohoAuthToken,
			CrmLeadDto remoteLead, CrmLeadNoteDto remoteLeadNoteDto);

	public abstract void updateLeadNote(String zohoAuthToken,
			CrmLeadDto remoteLead, CrmLeadNoteDto remoteLeadNoteDto);

	public abstract void deleteLeadNote(String zohoAuthToken, CrmLeadNoteDto note);

	public abstract void createLeadActivity(String zohoAuthToken,
			CrmLeadDto remoteLead, CrmLeadActivityDto remoteLeadActivityDto);

	public abstract void updateLeadActivity(String zohoAuthToken,
			CrmLeadDto remoteLead, CrmLeadActivityDto remoteLeadActivityDto);

	public abstract void deleteLeadActivity(String zohoAuthToken,
			CrmLeadActivityDto activity);
}