package com.similan.domain.repository.template;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.template.VelocityDocumentTemplate;
import com.similan.domain.entity.template.VelocityTemplateAttribute;
import com.similan.domain.repository.template.jpa.JpaVelocityTemplateAttributeRepository;
import com.similan.domain.repository.template.jpa.JpaVelocityTemplateRepository;

@Repository
public class VelocityTemplateRepository {
  @Autowired
  private JpaVelocityTemplateRepository repository;
  @Autowired
  private JpaVelocityTemplateAttributeRepository attrRepository;

	public VelocityDocumentTemplate findByName(String name) {
    return repository.findByName(name);
  }

	public List<VelocityDocumentTemplate> findAll() {
    return repository.findAll();
  }
	
	public void deleteAllInBatch() {
	  attrRepository.deleteAllInBatch();
    repository.deleteAllInBatch();
  }

	public VelocityDocumentTemplate save(
			VelocityDocumentTemplate entity) {
    return repository.save(
			entity);
  }
	
	public void deleteEmailTemplates() {
    repository.deleteEmailTemplates();
  }

	public VelocityDocumentTemplate create() {
		return new VelocityDocumentTemplate();
	}
	
	public VelocityTemplateAttribute createAttribute() {
		return new VelocityTemplateAttribute();
	}

}
