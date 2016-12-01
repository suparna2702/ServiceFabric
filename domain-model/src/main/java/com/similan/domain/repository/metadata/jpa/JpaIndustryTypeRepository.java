package com.similan.domain.repository.metadata.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.metadata.IndustryType;

public interface JpaIndustryTypeRepository extends
                                      JpaRepository<IndustryType, Long>{
	
	public List<IndustryType> findAll();
	
	public void deleteAllInBatch();

}
