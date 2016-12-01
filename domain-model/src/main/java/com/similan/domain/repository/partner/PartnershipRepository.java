package com.similan.domain.repository.partner;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.partner.PartnerProgramDefinition;
import com.similan.domain.entity.partner.Partnership;
import com.similan.domain.repository.partner.jpa.JpaPartnershipRepository;

@Repository
public class PartnershipRepository {
  @Autowired
  private JpaPartnershipRepository repository;

  public Partnership save(Partnership partnership) {
    return repository.save(partnership);
  }

  public Long findPartnershipCountByPartnerProgram(Long programId) {
    return repository.findPartnershipCountByPartnerProgram(programId);
  }

  public Long findPartnershipCount() {
    return repository.findPartnershipCount();
  }

  public List<Partnership> findPartnershipByPartnerProgram(Long programId) {
    return repository.findPartnershipByPartnerProgram(programId);
  }

  public List<Partnership> findPartnershipForBusiness(
      SocialOrganization socialOrg) {
    return repository.findPartnershipForBusiness(socialOrg);
  }

  public Partnership findOne(Long id) {
    return repository.findOne(id);
  }

  public void delete(Long partnershipId) {
    repository.delete(partnershipId);
  }

  public Partnership getPartnershipByProgramAndPartner(Long orgId, Long progId) {
    return repository.getPartnershipByProgramAndPartner(orgId, progId);
  }

  public List<Partnership> findPartnershipByProgramOwnerAndPartner(
      Long ownerId, Long partnerId) {
    return this.repository.findPartnershipByProgramOwnerAndPartner(ownerId,
        partnerId);
  }

  public Partnership create(PartnerProgramDefinition programDef,
      SocialOrganization org) {
    Partnership partnership = new Partnership();

    partnership.setCreated(new Date());
    partnership.setPartner(org);
    partnership.setPartnerProgram(programDef);
    return partnership;
  }

  public Partnership findByPartnerProgramAndPartner(
      PartnerProgramDefinition program, SocialOrganization parner) {
    return repository.findByPartnerProgramAndPartner(program, parner);
  }
}
