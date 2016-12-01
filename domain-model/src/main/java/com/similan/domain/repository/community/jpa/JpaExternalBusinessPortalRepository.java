package com.similan.domain.repository.community.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.ExternalBusinessPortal;
import com.similan.domain.entity.community.SocialOrganization;

public interface JpaExternalBusinessPortalRepository extends
    JpaRepository<ExternalBusinessPortal, Long> {

  @Query("delete from ExternalBusinessPortal where uuid= :uuid")
  public void delete(@Param("uuid") String uuid);

  public List<ExternalBusinessPortal> findByOwner(SocialOrganization owner);

}
