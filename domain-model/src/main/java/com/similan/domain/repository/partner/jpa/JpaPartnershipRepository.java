package com.similan.domain.repository.partner.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.partner.PartnerProgramDefinition;
import com.similan.domain.entity.partner.Partnership;

public interface JpaPartnershipRepository extends
    JpaRepository<Partnership, Long> {

  @Query("select partnership from Partnership partnership where partnership.partner=:socialOrg")
  public List<Partnership> findPartnershipForBusiness(
      @Param("socialOrg") SocialOrganization socialOrg);

  @Query("select partnership from Partnership partnership where partnership.partnerProgram.id=:programId")
  public List<Partnership> findPartnershipByPartnerProgram(
      @Param("programId") Long programId);

  @Query("select count(partnership) from Partnership partnership where partnership.partnerProgram.id=:programId")
  public Long findPartnershipCountByPartnerProgram(
      @Param("programId") Long programId);

  @Query("select count(partnership) from Partnership partnership")
  public Long findPartnershipCount();

  @Query("select partnership from Partnership partnership where partnership.partnerProgram.id=:progId and partnership.partner.id=:orgId")
  public Partnership getPartnershipByProgramAndPartner(
      @Param("orgId") Long orgId, @Param("progId") Long progId);

  @Query("select partnership from Partnership partnership where partnership.partnerProgram.programOwner.id=:ownerId"
      + " and partnership.partner.id=:partnerId")
  public List<Partnership> findPartnershipByProgramOwnerAndPartner(
      @Param("ownerId") Long ownerId, @Param("partnerId") Long partnerId);

  public Partnership findByPartnerProgramAndPartner(
      PartnerProgramDefinition program, SocialOrganization parner);
}
