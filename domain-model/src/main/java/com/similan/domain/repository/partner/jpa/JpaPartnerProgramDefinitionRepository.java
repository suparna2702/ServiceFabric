package com.similan.domain.repository.partner.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.partner.PartnerProgramDefinition;

public interface JpaPartnerProgramDefinitionRepository extends
                           JpaRepository<PartnerProgramDefinition, Long> {
	
	@Query("select partnProgram from PartnerProgramDefinition partnProgram where partnProgram.programOwner=:owner")
	public List<PartnerProgramDefinition> getProgramsForOwner(@Param("owner")SocialOrganization owner);
	
	@Query("select partnProgram from PartnerProgramDefinition partnProgram where partnProgram.programOwner=:owner "
			+ "and partnProgram.name = :name")
	public PartnerProgramDefinition findByName(@Param("name")String name, @Param("owner")SocialOrganization owner);
	
	@Query("select count(partnerProgram) from PartnerProgramDefinition partnerProgram")
	public Long findPartnerProgramCount();

  public int countByProgramOwner(SocialOrganization owner);


}
