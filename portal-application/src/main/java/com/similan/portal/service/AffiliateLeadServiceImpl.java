package com.similan.portal.service;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.util.AddressDto;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.lead.AffiliateLeadListDto;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.dto.lead.LeadImport;
import com.similan.framework.importexport.csv.CsvJavaBeanImporter;
import com.similan.framework.util.SpringUtils;
import com.similan.service.api.LeadManagementService;

@Service("affiliateLeadService")
@Slf4j
public class AffiliateLeadServiceImpl extends BaseService implements AffiliateLeadService {
	

	private static final long serialVersionUID = 1L;

	
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

	public List<LeadDto> handleLeadFileUpload(FileUploadEvent event) {
		
		CsvJavaBeanImporter memberImporter = (CsvJavaBeanImporter)SpringUtils.getSpringBean("basicCsvAffiliateLeadImporter");
		List<LeadDto> leadDtoList = new ArrayList<LeadDto>();
		if(event != null){
			log.info("Uploaded file name " + event.getFile().getFileName());
			try {
				InputStreamReader streamReader = new InputStreamReader(event.getFile().getInputstream());
				List<Object> leadList = memberImporter.importFromCsv(streamReader);
				
				/* iterate through the list and put it in lead import list */
				for(Object obj : leadList){
					LeadImport leadImport = (LeadImport)obj;
					LeadDto leadDto = new LeadDto();
					leadDto.setFirstName(leadImport.getFirstName());
					leadDto.setLastName(leadImport.getLastName());
					leadDto.setCompany(leadImport.getCompany());
					leadDto.setContactEmail(leadImport.getEmail());
					leadDto.setContactPhone(leadImport.getPhone());
					leadDto.setIndustry(leadImport.getIndustry());
					leadDto.setKeyword(leadImport.getKeyword());
					leadDto.setTitle(leadImport.getTitle());
					leadDto.setDescription(leadImport.getDescription());
				
					AddressDto address = new AddressDto();
					address.setStreet(leadImport.getStreet());
					address.setCity(leadImport.getCity());
					address.setState(leadImport.getState());
					address.setZipCode(leadImport.getZip());
					address.setCountry(leadImport.getCountry());					
					leadDto.setLocation(address);
					
					leadDtoList.add(leadDto);
				}
				log.info("Number of members uploaded " + leadDtoList.size());
				
			} catch(Exception exp){
				log.error("Import error in bulk invite ", exp);
			}
		}
		return leadDtoList;
	}
	
	@Transactional
	public void purchaseAffilateLead(OrganizationDetailInfoDto orgInfo, 
			                         LeadDto affLeadDto){
		log.info("Purchaing affiliate lead for org " + orgInfo.getId() 
				     + " lead id " + affLeadDto.getId());
		this.leadManagementService.purchaseAffilateLead(orgInfo, affLeadDto);
	}
	
	@Transactional
	public List<LeadDto> getAffiliateLeadsByFilter(Long filterId) throws Exception{
		return this.leadManagementService.getAffiliateLeadsByFilter(filterId);
	}
	
}
