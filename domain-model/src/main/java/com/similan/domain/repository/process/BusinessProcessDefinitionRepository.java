package com.similan.domain.repository.process;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.process.BusinessProcessDefinition;
import com.similan.domain.repository.process.jpa.JpaBusinessProcessDefinitionRepository;


@Repository
public class BusinessProcessDefinitionRepository {
	@Autowired
	private JpaBusinessProcessDefinitionRepository repository;

  public BusinessProcessDefinition findByName(String name) {
    return repository.findByName(name);
  }
	
	public List<BusinessProcessDefinition> findAll() {
    return repository.findAll();
  }

	public BusinessProcessDefinition save(BusinessProcessDefinition businessProcessDefinition) {
    return repository.save(businessProcessDefinition);
  }
	
	public BusinessProcessDefinition create() {
		return new BusinessProcessDefinition();
	}

}
