package com.similan.adminapp.service;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.lead.AffiliateLeadListDto;
import com.similan.framework.dto.lead.LeadImport;
import com.similan.framework.importexport.csv.CsvJavaBeanImporter;
import com.similan.framework.util.SpringUtils;
import com.similan.service.api.LeadManagementService;

@Service("affiliateLeadService")
@Slf4j
public class AffiliateLeadServiceImpl implements AffiliateLeadService {
	
	
	@Autowired
	private LeadManagementService leadManagementService;

	@Transactional
	public void saveAffiliateLeadList(AffiliateLeadListDto uploadLeadList) throws Exception {
		leadManagementService.saveAffiliateLeadList(uploadLeadList);
		
	}

	public LeadManagementService getLeadManagementService() {
		return leadManagementService;
	}

	public void setLeadManagementService(LeadManagementService leadManagementService) {
		this.leadManagementService = leadManagementService;
	}
	
	@Transactional
	public List<AffiliateLeadListDto> getAffiliateLeadListForBusiness(OrganizationDetailInfoDto orgInfo){
		return this.leadManagementService.getAffiliateLeadListForBusiness(orgInfo);
	}
	
	@Transactional
	public AffiliateLeadListDto getAffiliateLeadListDetail(Long listId) throws Exception {
		return this.leadManagementService.getAffiliateLeadListDetail(listId);
	}

	public List<LeadImport> handleLeadFileUpload(FileUploadEvent event) {
		
		CsvJavaBeanImporter memberImporter = (CsvJavaBeanImporter)SpringUtils.getSpringBean("basicCsvAffiliateLeadImporter");
		List<LeadImport> importLeadList = new ArrayList<LeadImport>();
		
		if(event != null){
			log.info("Uploaded file name " + event.getFile().getFileName());
			try {
				InputStreamReader streamReader = new InputStreamReader(event.getFile().getInputstream());
				List<Object> leadList = memberImporter.importFromCsv(streamReader);
				
				/* iterate through the list and put it in lead import list */
				for(Object obj : leadList){
					LeadImport leadImport = (LeadImport)obj;
					importLeadList.add(leadImport);
				}
				log.info("Number of members uploaded " + importLeadList.size());
				
			} catch(Exception exp){
				log.error("Import error in bulk invite ", exp);
			}
		}
		return importLeadList;
	}
	
}
