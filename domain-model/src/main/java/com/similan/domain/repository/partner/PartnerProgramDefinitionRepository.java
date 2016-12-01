package com.similan.domain.repository.partner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.partner.PartnerPreQualificationQuestion;
import com.similan.domain.entity.partner.PartnerProgramDefinition;
import com.similan.domain.entity.partner.PartnerProgramRequiredAttribute;
import com.similan.domain.repository.partner.jpa.JpaPartnerProgramDefinitionRepository;

@Repository
public class PartnerProgramDefinitionRepository {
  @Autowired
  private JpaPartnerProgramDefinitionRepository repository;
	
	public PartnerProgramDefinition save(PartnerProgramDefinition entity) {
    return repository.save(entity);
  }
	
	public List<PartnerProgramDefinition> getProgramsForOwner(SocialOrganization owner) {
    return repository.getProgramsForOwner(owner);
  }
	
	public List<PartnerProgramDefinition> findAll() {
    return repository.findAll();
  }
	
	public Long findPartnerProgramCount() {
    return repository.findPartnerProgramCount();
  }

	public PartnerProgramDefinition findOne(Long id) {
    return repository.findOne(id);
  }
	
	public PartnerProgramDefinition findByName(String name, SocialOrganization owner) {
    return repository.findByName(name, owner);
  }
	
	public PartnerProgramDefinition create() {
		PartnerProgramDefinition programDef = new PartnerProgramDefinition();
		return programDef;
	}
	
	public PartnerProgramRequiredAttribute createAttribute(){
		PartnerProgramRequiredAttribute attr = new PartnerProgramRequiredAttribute();
		return attr;
	}
	
	public PartnerPreQualificationQuestion createPrequalQuestion(){
		PartnerPreQualificationQuestion question = new PartnerPreQualificationQuestion();
		return question;
	}

  public int countByOwner(SocialOrganization owner) {
    return repository.countByProgramOwner(owner);
  }

}
