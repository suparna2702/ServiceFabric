package com.similan.domain.repository.collaborationworkspace.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;

public interface JpaCollaborationWorkspaceRepository extends
    JpaRepository<CollaborationWorkspace, Long> {

  @Query("select workspace from CollaborationWorkspace workspace"
      + " where workspace.owner.name = :ownerName"
      + " and workspace.name = :name")
  CollaborationWorkspace findOne(@Param("ownerName") String ownerName,
      @Param("name") String name);

  @Query("select workspace from CollaborationWorkspace workspace"
      + " where workspace.owner.name = :ownerName")
  public List<CollaborationWorkspace> findAllByOwner(
      @Param("ownerName") String ownerName);

  @Query("select workspace from CollaborationWorkspace workspace"
      + " where workspace.owner = :owner")
  public List<CollaborationWorkspace> findAllByOwner(
      @Param("owner") SocialActor owner);

  public List<CollaborationWorkspace> findByOwnerAndPartnerWorkspace(
      SocialActor owner, Boolean partnerWorkspace);

  @Query("select count(*) from CollaborationWorkspace workspace"
      + " where workspace.owner = :owner")
  int countByOwner(@Param("owner") SocialOrganization owner);

}
