package com.similan.domain.repository.collaborationworkspace;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceStatus;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.repository.collaborationworkspace.jpa.JpaCollaborationWorkspaceRepository;

@Repository
public class CollaborationWorkspaceRepository {
  @Autowired
  private JpaCollaborationWorkspaceRepository repository;

  public CollaborationWorkspace save(CollaborationWorkspace entity) {
    return repository.save(entity);
  }

  public List<CollaborationWorkspace> findByOwnerAndPartnerWorkspace(
      SocialActor owner, Boolean partnerWorkspace) {
    return repository.findByOwnerAndPartnerWorkspace(owner, partnerWorkspace);
  }

  public CollaborationWorkspace findOne(Long id) {
    return repository.findOne(id);
  }

  public CollaborationWorkspace findOne(String ownerName, String name) {
    return repository.findOne(ownerName, name);
  }

  public List<CollaborationWorkspace> findAllByOwner(String ownerName) {
    return repository.findAllByOwner(ownerName);
  }

  public List<CollaborationWorkspace> findAllByOwner(SocialActor owner) {
    return repository.findAllByOwner(owner);
  }

  public CollaborationWorkspace create(SocialActor owner, String name,
      SocialActor creator, String description,
      CollaborationWorkspaceStatus status) {
    CollaborationWorkspace workspace = new CollaborationWorkspace(name,
        description, status);
    workspace.setOwner(owner);
    workspace.setCreator(creator);
    workspace.setTimeStamp(new Date());
    return workspace;
  }

  public int countByOwner(SocialOrganization owner) {
    return repository.countByOwner(owner);
  }

}
