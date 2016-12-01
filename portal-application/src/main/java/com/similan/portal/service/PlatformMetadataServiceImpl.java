package com.similan.portal.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.metadata.CountryType;
import com.similan.domain.entity.metadata.IndustryType;
import com.similan.domain.entity.metadata.StateType;
import com.similan.domain.repository.metadata.CountryTypeRepository;
import com.similan.domain.repository.metadata.IndustryTypeRepository;
import com.similan.framework.dto.Image;
import com.similan.service.impl.UtilityDataServiceImpl;

@Service("platformMetadataService")
@Slf4j
public class PlatformMetadataServiceImpl extends BaseService implements PlatformMetadataService {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private UtilityDataServiceImpl utilityDataService;
			
	protected PlatformMetadataServiceImpl(){
		log.info("Platform service created");
	}
	
	public UtilityDataServiceImpl getUtilityDataService() {
		return utilityDataService;
	}

	public void setUtilityDataService(UtilityDataServiceImpl utilityDataService) {
		this.utilityDataService = utilityDataService;
	}

	@Transactional
	public List<IndustryType> getAllIndustryType(){
		IndustryTypeRepository industryRepo = (IndustryTypeRepository)this.getSpringBean("industryTypeRepository");
		return industryRepo.findAll();
	}
	
	@Transactional
	public List<Image> getHomePageSlideshowImages(){
		return this.utilityDataService.getHomePageSlideshowImages();
	}
	
	
	@Transactional
	public List<CountryType> getAllCountry(){
		CountryTypeRepository countryRepo = (CountryTypeRepository)this.getSpringBean("countryTypeRepository");
		List<CountryType> countryList =countryRepo.findAll();
		
		for(CountryType country : countryList){
			List<StateType> stateList = country.getStates();
			
			for(StateType state : stateList){
				log.info("State name : " + state.getStateName());
			}
		}
		
		return countryList;
	}
	
}
