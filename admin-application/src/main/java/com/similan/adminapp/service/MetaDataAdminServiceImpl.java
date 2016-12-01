package com.similan.adminapp.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.metadata.IndustryType;
import com.similan.domain.repository.metadata.IndustryTypeRepository;
import com.similan.domain.repository.template.VelocityTemplateRepository;
import com.similan.framework.dto.metadata.IndustryTypeDto;
import com.similan.framework.dto.update.SystemUpdateEventDto;
import com.similan.framework.util.SpringUtils;
import com.similan.service.impl.DomainEventManagementServiceImpl;

@Service("metaDataAdminService")
public class MetaDataAdminServiceImpl implements MetaDataAdminService, Serializable {
	private static final long serialVersionUID = 1L;
	
  @Autowired
  private VelocityTemplateRepository templateRepository;
  @Autowired
  private IndustryTypeRepository industryRepo;
	
	@Transactional
	public List<IndustryTypeDto> getIndustryList() {
		List<IndustryTypeDto> dtoList = new ArrayList<IndustryTypeDto>();
		for(IndustryType insdustryType : industryRepo.findAll()) {
			IndustryTypeDto industryTypeDto = new IndustryTypeDto();
			industryTypeDto.setId(insdustryType.getId());
			industryTypeDto.setIndustryName(insdustryType.getInsdustryName());
			dtoList.add(industryTypeDto);
		}
		return dtoList;
	}

	@Transactional
	public List<SystemUpdateEventDto> getAllSystemUpdates() {
		DomainEventManagementServiceImpl domainEventService = (DomainEventManagementServiceImpl)SpringUtils.getSpringBean("domainEventManagementManagementService");
		return domainEventService.getAllSystemEvents();
	}
	
	@Transactional
	public void deleteSystemUpdate(SystemUpdateEventDto update){
		DomainEventManagementServiceImpl domainEventService = (DomainEventManagementServiceImpl)SpringUtils.getSpringBean("domainEventManagementManagementService");
		domainEventService.deleteSystemUpdate(update);
	}
	
	@Transactional
	public void saveSystemUpdate(SystemUpdateEventDto updateItem){
		DomainEventManagementServiceImpl domainEventService = (DomainEventManagementServiceImpl)SpringUtils.getSpringBean("domainEventManagementManagementService");
		domainEventService.saveSystemUpdate(updateItem);
	}
    

}
