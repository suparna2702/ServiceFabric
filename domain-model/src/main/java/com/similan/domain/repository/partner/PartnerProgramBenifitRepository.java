package com.similan.domain.repository.partner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.partner.PartnerProgramBenifit;
import com.similan.domain.repository.partner.jpa.JpaPartnerProgramBenifitRepository;

@Repository
public class PartnerProgramBenifitRepository {
  @Autowired
  private JpaPartnerProgramBenifitRepository repository;
	
	public PartnerProgramBenifit save(PartnerProgramBenifit benifit) {
    return repository.save(benifit);
  }
	
	public PartnerProgramBenifit findOne(Long id) {
    return repository.findOne(id);
  }
	
	public PartnerProgramBenifit create(String benifitName, String benifitDetails){
		PartnerProgramBenifit benifit = new PartnerProgramBenifit();
		
		benifit.setBenifitDetails(benifitDetails);
		benifit.setBenifitName(benifitName);
		return benifit;
	}

}
