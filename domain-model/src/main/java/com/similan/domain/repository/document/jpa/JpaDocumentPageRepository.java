package com.similan.domain.repository.document.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.document.DocumentPage;

public interface JpaDocumentPageRepository extends
    JpaRepository<DocumentPage, Long> {

  @Query("select page from DocumentPage page"
      + " where page.documentInstance.document.workspace.owner.name = :workspaceOwnerName"
      + " and page.documentInstance.document.workspace.name = :workspaceName"
      + " and page.documentInstance.document.name = :documentName"
      + " and page.documentInstance.version = :documentInstanceVersion"
      + " and page.documentInstance.document.deletionDate is null"
      + " and page.number = :number")
  DocumentPage findOne(
      @Param("workspaceOwnerName") String workspaceOwnerName,
      @Param("workspaceName") String workspaceName,
      @Param("documentName") String documentName,
      @Param("documentInstanceVersion") int documentInstanceVersion,
      @Param("number") int number);

}
