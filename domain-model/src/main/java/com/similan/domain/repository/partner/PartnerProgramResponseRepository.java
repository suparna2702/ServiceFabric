package com.similan.domain.repository.partner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.partner.PartnerProgramResponse;
import com.similan.domain.repository.partner.jpa.JpaPartnerProgramResponseRepository;

@Repository
public class PartnerProgramResponseRepository {
  @Autowired
  private JpaPartnerProgramResponseRepository repository;
	
	public PartnerProgramResponse save(PartnerProgramResponse response) {
    return repository.save(response);
  }
	public PartnerProgramResponse findOne(Long id) {
    return repository.findOne(id);
  }
	
	public PartnerProgramResponse create(){
		PartnerProgramResponse response = new PartnerProgramResponse();
		return response;
	}

}
