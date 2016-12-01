package com.similan.domain.repository.document.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.DocumentInstance;

public interface JpaDocumentInstanceRepository extends
    JpaRepository<DocumentInstance, Long> {

  @Query("select documentInstance from DocumentInstance documentInstance"
      + " where documentInstance.document.workspace.owner.name = :workspaceOwnerName"
      + " and documentInstance.document.workspace.name = :workspaceName"
      + " and documentInstance.document.deletionDate is null"
      + " and documentInstance.document.name = :documentName"
      + " and documentInstance.version = :version")
  DocumentInstance findOne(
      @Param("workspaceOwnerName") String workspaceOwnerName,
      @Param("workspaceName") String workspaceName,
      @Param("documentName") String documentName, @Param("version") int version);

  @Query("select coalesce(sum(documentInstance.originalAsset.size),0) from DocumentInstance documentInstance"
      + " where documentInstance.document.workspace.owner = :owner")
  long totalSizeByOwner(@Param("owner") SocialActor owner);
}
