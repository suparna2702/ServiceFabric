package com.similan.domain.repository.partner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.partner.PartnerProgramRequiredAttribute;
import com.similan.domain.repository.partner.jpa.JpaPartnerProgramRequiredAttributeRepository;

@Repository
public class PartnerProgramRequiredAttributeRepository {
  @Autowired
  private JpaPartnerProgramRequiredAttributeRepository repository;
	
	public PartnerProgramRequiredAttribute save(PartnerProgramRequiredAttribute attr) {
    return repository.save(attr);
  }
	
	public PartnerProgramRequiredAttribute findOne(Long id) {
    return repository.findOne(id);
  }
	
	public void delete(Long attrId) {
    repository.delete(attrId);
  }
	
	public PartnerProgramRequiredAttribute create(){
		PartnerProgramRequiredAttribute attr = new PartnerProgramRequiredAttribute();
		return attr;
	}

}
