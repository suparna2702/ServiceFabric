package com.similan.domain.repository.metadata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.metadata.IndustryType;
import com.similan.domain.repository.metadata.jpa.JpaIndustryTypeRepository;

@Repository
public class IndustryTypeRepository {
  @Autowired
  private JpaIndustryTypeRepository repository;
	
	public List<IndustryType> findAll() {
    return repository.findAll();
  }
	
	public IndustryType save(IndustryType industry) {
    return repository.save(industry);
  }
	
	public void deleteAllInBatch() {
    repository.deleteAllInBatch();
  }
	
	public IndustryType create(String name){
		IndustryType industry = new IndustryType();
		industry.setInsdustryName(name);
		return industry;
	}

}
