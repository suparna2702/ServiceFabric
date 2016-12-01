package com.similan.domain.repository.metadata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.metadata.CountryType;
import com.similan.domain.entity.metadata.StateType;
import com.similan.domain.repository.metadata.jpa.JpaCountryTypeRepository;

@Repository
public class CountryTypeRepository {
  @Autowired
  private JpaCountryTypeRepository repository;
	
	public List<CountryType> findAll() {
    return repository.findAll();
  }
	
	public CountryType save(CountryType country) {
    return repository.save(country);
  }
	
	public void deleteAllInBatch() {
    repository.deleteAllInBatch();
  }
	
	public CountryType create(String name){
		CountryType country = new CountryType();
		country.setCountryName(name);
		return country;
	}
	
	public StateType createState(String stateName, String stateCode){
		StateType state = new StateType();
		state.setStateCode(stateCode);
		state.setStateName(stateName);
		return state;
	}

}
