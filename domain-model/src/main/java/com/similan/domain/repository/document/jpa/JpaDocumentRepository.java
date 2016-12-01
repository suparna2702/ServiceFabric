package com.similan.domain.repository.document.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;

public interface JpaDocumentRepository extends JpaRepository<Document, Long> {

  @Query("select document from Document document"
      + " where document.workspace.owner.name = :workspaceOwnerName"
      + " and document.workspace.name = :workspaceName"
      + " and document.deletionDate is null" + " and document.name = :name")
  Document findOne(@Param("workspaceOwnerName") String workspaceOwnerName,
      @Param("workspaceName") String workspaceName, @Param("name") String name);

  @Query("select document from Document document"
      + " where document.workspace.owner.name = :workspaceOwnerName"
      + " and document.workspace.name = :workspaceName"
      + " and document.deletionDate is null" + " and document.name = :name")
  List<Document> find(@Param("workspaceOwnerName") String workspaceOwnerName,
      @Param("workspaceName") String workspaceName, @Param("name") String name);

  @Query("select document from Document document"
      + " where document.workspace = :workspace"
      + " and document.deletionDate is null"
      + " and document.name = :name")
  List<Document> find(@Param("workspace") ManagementWorkspace workspace,
      @Param("name") String name);

  @Query("select count(document) from Document document"
      + " where document.workspace.owner = :owner")
  int countByOwner(@Param("owner") SocialActor owner);

  @Query("select count(document) from Document document"
      + " where document.workspace = :workspace"
      + " and document.deletionDate is null")
  int countByManagementWorkspace(
      @Param("workspace") ManagementWorkspace workspace);
}
