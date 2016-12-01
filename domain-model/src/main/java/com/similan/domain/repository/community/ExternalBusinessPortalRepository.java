package com.similan.domain.repository.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.ExternalBusinessPortal;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.repository.community.jpa.JpaExternalBusinessPortalRepository;

@Repository
public class ExternalBusinessPortalRepository {

  @Autowired
  private JpaExternalBusinessPortalRepository repository;

  public List<ExternalBusinessPortal> findByOwner(SocialOrganization owner) {
    return repository.findByOwner(owner);
  }

  public ExternalBusinessPortal save(ExternalBusinessPortal portal) {
    return repository.save(portal);
  }

  public ExternalBusinessPortal findOne(Long id) {
    return repository.findOne(id);
  }

  public void delete(String uuid) {
    repository.delete(uuid);
  }
  
  public void delete(ExternalBusinessPortal portal){
    repository.delete(portal);
  }

  public ExternalBusinessPortal create(SocialOrganization owner,
      String poralName, String portalUrl) {

    ExternalBusinessPortal portal = new ExternalBusinessPortal();
    portal.setPortalName(poralName);
    portal.setPortalUrl(portalUrl);
    portal.setOwner(owner);
    return portal;
  }

}
