package com.similan.domain.repository.collaborationworkspace.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;

public interface JpaSharedDocumentRepository extends
    JpaRepository<SharedDocument, Long> {

  @Query("select sharedDocument from SharedDocument sharedDocument"
      + " where sharedDocument.workspace.owner.name = :workspaceOwnerName"
      + " and sharedDocument.workspace.name = :workspaceName"
      + " and sharedDocument.document.workspace.name = :documentWorkspaceName"
      + " and sharedDocument.document.name = :documentName")
  SharedDocument findOne(
      @Param("workspaceOwnerName") String workspaceOwnerName,
      @Param("workspaceName") String workspaceName,
      @Param("documentWorkspaceName") String documentWorkspaceName,
      @Param("documentName") String documentName);

  @Query("select sharedDocument from SharedDocument sharedDocument"
      + " where sharedDocument.workspace =:workspace"
      + " order by sharedDocument.document.name, sharedDocument.id")
  List<SharedDocument> findByWorkspace(
      @Param("workspace") CollaborationWorkspace workspace);

  @Query("select sharedDocument from SharedDocument sharedDocument"
      + " where sharedDocument.creator =:sharer"
      + " order by sharedDocument.document.name, sharedDocument.id")
  List<SharedDocument> findBySharer(@Param("sharer") SocialActor sharer);

  @Query("select sharedDocument from SharedDocument sharedDocument"
      + " where sharedDocument.document =:document")
  List<SharedDocument> findAllForDocument(@Param("document") Document document);

  @Query("select sharedDocument from SharedDocument sharedDocument"
      + " where sharedDocument.document =:document"
      + " and sharedDocument.workspace = :workspace")
  public List<SharedDocument> findSharedDocumentInWorkspace(
      @Param("document") Document document,
      @Param("workspace") CollaborationWorkspace workspace);

  @Query("select sharedDocument from SharedDocument sharedDocument"
      + " where sharedDocument.document.name =:documentName"
      + " and sharedDocument.workspace = :workspace")
  public List<SharedDocument> findSharedDocumentInWorkspace(
      @Param("documentName") String documentName,
      @Param("workspace") CollaborationWorkspace workspace);

}
