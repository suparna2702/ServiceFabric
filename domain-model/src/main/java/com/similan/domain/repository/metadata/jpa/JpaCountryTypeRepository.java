package com.similan.domain.repository.metadata.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.metadata.CountryType;

public interface JpaCountryTypeRepository extends
                                       JpaRepository<CountryType, Long>{
	public List<CountryType> findAll();
	
	public void deleteAllInBatch();
	
}
