package com.similan.domain.repository.document.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.document.DocumentViewerItem;

public interface JpaDocumentViewerItemRepository extends
    JpaRepository<DocumentViewerItem, Long> {

  @Query("select item from DocumentViewerItem item"
      + " where item.documentInstance.document.workspace.owner.name = :workspaceOwnerName"
      + " and item.documentInstance.document.workspace.name = :workspaceName"
      + " and item.documentInstance.document.deletionDate is null"
      + " and item.documentInstance.document.name = :documentName"
      + " and item.documentInstance.version = :documentInstanceVersion"
      + " and item.name = :name")
  DocumentViewerItem findOne(
      @Param("workspaceOwnerName") String workspaceOwnerName,
      @Param("workspaceName") String workspaceName,
      @Param("documentName") String documentName,
      @Param("documentInstanceVersion") Integer documentInstanceVersion,
      @Param("name") String name);
}
