package com.similan.domain.repository.collaborationworkspace;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.repository.collaborationworkspace.jpa.JpaSharedDocumentRepository;

@Repository
public class SharedDocumentRepository {
  @Autowired
  private JpaSharedDocumentRepository repository;

  public SharedDocument findOne(Long id) {
    return repository.findOne(id);
  }

  public SharedDocument findOne(String workspaceOwnerName,
      String workspaceName, String documentWorkspaceName, String documentName) {
    return repository.findOne(workspaceOwnerName, workspaceName,
        documentWorkspaceName, documentName);
  }

  public List<SharedDocument> findAll() {
    return repository.findAll();
  }

  public SharedDocument save(SharedDocument sharedDocument) {
    return repository.save(sharedDocument);
  }

  public List<SharedDocument> findAllForDocument(Document document) {
    return repository.findAllForDocument(document);
  }

  public List<SharedDocument> findByWorkspace(CollaborationWorkspace workspace) {
    return repository.findByWorkspace(workspace);
  }

  public List<SharedDocument> findSharedDocumentInWorkspace(Document document,
      CollaborationWorkspace workspace) {
    return repository.findSharedDocumentInWorkspace(document, workspace);
  }

  public List<SharedDocument> findSharedDocumentInWorkspace(
      String documentName, CollaborationWorkspace workspace) {
    return repository.findSharedDocumentInWorkspace(documentName, workspace);
  }

  public List<SharedDocument> findBySharer(SocialActor sharer) {
    return repository.findBySharer(sharer);
  }

  public SharedDocument create(CollaborationWorkspace workspace,
      SocialActor initiator, Date creationDate, Document document) {
    SharedDocument sharedDocument = new SharedDocument(creationDate);
    sharedDocument.setWorkspace(workspace);
    sharedDocument.setCreator(initiator);
    sharedDocument.setDocument(document);
    return sharedDocument;
  }

}
