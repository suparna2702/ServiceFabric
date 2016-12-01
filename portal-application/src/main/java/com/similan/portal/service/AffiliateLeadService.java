package com.similan.portal.service;

import java.util.List;

import org.primefaces.event.FileUploadEvent;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.lead.AffiliateLeadListDto;
import com.similan.framework.dto.lead.LeadDto;

public interface AffiliateLeadService {
	
	public void saveAffiliateLeadList(AffiliateLeadListDto uploadLeadList) throws Exception;

	public List<LeadDto> handleLeadFileUpload(FileUploadEvent event);
	
	public List<AffiliateLeadListDto> getAffiliateLeadListForBusiness(OrganizationDetailInfoDto orgInfo);

	public AffiliateLeadListDto getAffiliateLeadListDetail(Long listId) throws Exception;
	
	public void purchaseAffilateLead(OrganizationDetailInfoDto orgInfo, 
			                         LeadDto affLeadDto);
	
	public List<LeadDto> getAffiliateLeadsByFilter(Long filterId) throws Exception;

}
