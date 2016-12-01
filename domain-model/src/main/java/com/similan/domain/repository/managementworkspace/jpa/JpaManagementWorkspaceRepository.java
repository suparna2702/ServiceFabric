package com.similan.domain.repository.managementworkspace.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;

public interface JpaManagementWorkspaceRepository extends
    JpaRepository<ManagementWorkspace, Long> {

  @Query("select workspace from ManagementWorkspace workspace"
      + " where workspace.owner.name = :ownerName"
      + " and workspace.name = :name")
  ManagementWorkspace findOne(@Param("ownerName") String ownerName,
      @Param("name") String name);

  @Query("select document from Document document"
      + " where document.workspace.id = :id"
      + " and document.deletionDate is null")
  List<Document> findDocuments(@Param("id") Long id);

  @Query("select document from Document document"
      + " where document.workspace = :workspace"
      + " and document.name = :documentName")
  List<Document> findByManagementWorkspaceAndDocumentName(
      @Param("workspace") ManagementWorkspace workspace,
      @Param("documentName") String documentName);

  int countByOwner(SocialOrganization owner);

  List<ManagementWorkspace> findByNameAndOwner(String name, SocialActor owner);
}
