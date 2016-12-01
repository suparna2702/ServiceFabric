package com.similan.portal.databean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import lombok.extern.slf4j.Slf4j;

import com.similan.domain.entity.metadata.CountryType;
import com.similan.domain.entity.metadata.StateType;
import com.similan.portal.view.BaseView;

@ManagedBean(name = "countryBean")
@ApplicationScoped
@Slf4j
public class CountryBean extends BaseView {

	private static final long serialVersionUID = 1L;
	
	private List<CountryType> countryList = null;
	
	private Map<String, CountryType> countryMap = null;
	
	public Map<String, CountryType> getCountryMap() {
		return countryMap;
	}

	public void setCountryMap(Map<String, CountryType> countryMap) {
		this.countryMap = countryMap;
	}

	public List<CountryType> getCountryList() {
		return countryList;
	}
	
	public List<StateType> getStateTypeList(String countryName) {

		if (countryName == null)
			return new ArrayList<StateType>();
		List<StateType> retStateList = null;
		log.info("Getting states for country " + countryName);

		CountryType country = this.countryMap.get(countryName);
		if (country != null) {
			retStateList = country.getStates();
		} else {
			retStateList = new ArrayList<StateType>();
		}
		return retStateList;
	}

	public void setCountryList(List<CountryType> countryList) {
		this.countryList = countryList;
	}
	
	@PostConstruct
	public void init() {
		this.countryList = platformMetadataService.getAllCountry();
		this.countryMap = new HashMap<String, CountryType>();
		
		for(CountryType country : this.countryList){
			if(this.countryMap.containsKey(country.getCountryName()) != true){
				this.countryMap.put(country.getCountryName(), country);
			}
		}
		
		log.info("Getting number of countries " + this.countryList.size());
	}

}
