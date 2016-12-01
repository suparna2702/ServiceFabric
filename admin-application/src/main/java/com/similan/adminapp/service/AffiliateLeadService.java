package com.similan.adminapp.service;

import java.util.List;

import org.primefaces.event.FileUploadEvent;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.lead.AffiliateLeadListDto;
import com.similan.framework.dto.lead.LeadImport;

public interface AffiliateLeadService {
	
	public void saveAffiliateLeadList(AffiliateLeadListDto uploadLeadList) throws Exception;

	public List<LeadImport> handleLeadFileUpload(FileUploadEvent event);
	
	public List<AffiliateLeadListDto> getAffiliateLeadListForBusiness(OrganizationDetailInfoDto orgInfo);

	public AffiliateLeadListDto getAffiliateLeadListDetail(Long listId) throws Exception;

}
