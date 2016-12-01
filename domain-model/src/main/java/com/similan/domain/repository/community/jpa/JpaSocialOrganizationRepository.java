package com.similan.domain.repository.community.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.OrganizationType;
import com.similan.domain.entity.community.SocialMemberVisibilityType;
import com.similan.domain.entity.community.SocialOrganization;

public interface JpaSocialOrganizationRepository extends
    JpaRepository<SocialOrganization, Long> {

  @Query("select org from SocialOrganization org where org.businessName=:name")
  public SocialOrganization findOrgByName(@Param("name") String name);

  @Query("select org from SocialOrganization org where org.primaryEmail=:email")
  public SocialOrganization findOrgByEmail(@Param("email") String email);

  @Query("select org from SocialOrganization org where org.visibilityType=:visibility")
  public List<SocialOrganization> findAllOrgByVisibilityType(
      @Param("visibility") SocialMemberVisibilityType visibility);

  @Query("select count(org) from SocialOrganization org")
  public Long getTotalBusinessCount();

  @Query("select org from SocialOrganization org where org.businessName=:bizName")
  public List<SocialOrganization> findByName(@Param("bizName") String bizName);

  @Query("select count(org) from SocialOrganization org where org.organizationType=:orgType")
  public Long getOrganizationCountByType(
      @Param("orgType") OrganizationType orgType);

  @Query("select actor from SocialOrganization actor where actor.name = :name")
  public abstract SocialOrganization findOne(@Param("name") String name);

  public abstract SocialOrganization findByBusinessNameAndPrimaryEmail(
      String name, String email);
  
  public abstract SocialOrganization findByBusinessName(String businessName);

}
