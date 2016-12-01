package com.similan.domain.repository.managementworkspace;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.repository.managementworkspace.jpa.JpaManagementWorkspaceRepository;

@Repository
public class ManagementWorkspaceRepository {
  @Autowired
  private JpaManagementWorkspaceRepository repository;

  public ManagementWorkspace save(ManagementWorkspace entity) {
    return repository.save(entity);
  }

  public ManagementWorkspace findOne(Long id) {
    return repository.findOne(id);
  }

  public ManagementWorkspace findOne(String ownerName, String name) {
    return repository.findOne(ownerName, name);
  }

  public List<Document> findDocuments(Long id) {
    return repository.findDocuments(id);
  }

  public List<ManagementWorkspace> findByNameAndOwner(String name,
      SocialActor owner) {
    return repository.findByNameAndOwner(name, owner);
  }

  public List<Document> findByManagementWorkspaceAndDocumentName(
      ManagementWorkspace workspace, String documentName) {
    return repository.findByManagementWorkspaceAndDocumentName(workspace,
        documentName);
  }

  public ManagementWorkspace create(SocialActor owner, String name) {
    ManagementWorkspace workspace = new ManagementWorkspace(name);
    workspace.setOwner(owner);
    return workspace;
  }

  public int countByOwner(SocialOrganization owner) {
    return repository.countByOwner(owner);
  }

}
