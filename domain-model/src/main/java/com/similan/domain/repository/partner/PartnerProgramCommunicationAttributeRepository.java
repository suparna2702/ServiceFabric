package com.similan.domain.repository.partner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.partner.PartnerProgramCommunicationAttribute;
import com.similan.domain.entity.partner.PartnerProgramCommunicationAttributeType;
import com.similan.domain.repository.partner.jpa.JpaPartnerProgramCommunicationAttributeRepository;

@Repository
public class PartnerProgramCommunicationAttributeRepository {
  @Autowired
  private JpaPartnerProgramCommunicationAttributeRepository repository;
	
	public PartnerProgramCommunicationAttribute save(PartnerProgramCommunicationAttribute attr) {
    return repository.save(attr);
  }
	
	public List<PartnerProgramCommunicationAttribute> findAll() {
    return repository.findAll();
  }
	
	public PartnerProgramCommunicationAttribute findOne(Long id) {
    return repository.findOne(id);
  }
	
	public PartnerProgramCommunicationAttribute create(PartnerProgramCommunicationAttributeType attributeType, String attrValue){
		PartnerProgramCommunicationAttribute attr = new PartnerProgramCommunicationAttribute();
		
		attr.setAttributeType(attributeType);
		attr.setAttributeName(attrValue);
		
		return attr;
	}

}
