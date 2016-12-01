package com.similan.domain.repository.partner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.partner.PartnerProgramType;
import com.similan.domain.entity.partner.PartnerProgramTypeInfo;
import com.similan.domain.repository.partner.jpa.JpaPartnerProgramTypeInfoRepository;

@Repository
public class PartnerProgramTypeInfoRepository {
  @Autowired
  private JpaPartnerProgramTypeInfoRepository repository;
	
	public PartnerProgramTypeInfo save(PartnerProgramTypeInfo typeInfo) {
    return repository.save(typeInfo);
  }
	
	public List<PartnerProgramTypeInfo> findAll() {
    return repository.findAll();
  }
	
	public PartnerProgramTypeInfo findOne(Long id) {
    return repository.findOne(id);
  }
	
	public PartnerProgramTypeInfo create(PartnerProgramType programType, 
			String customProgramName, String programDetails){
		PartnerProgramTypeInfo typeInfo = new PartnerProgramTypeInfo();
		typeInfo.setProgramType(programType);
		typeInfo.setCustomProgramName(customProgramName);
		typeInfo.setProgramDetails(programDetails);
		return typeInfo;
		
	}

}
