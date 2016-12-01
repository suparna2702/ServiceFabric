package com.similan.domain.repository.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.document.DocumentInstance;
import com.similan.domain.repository.document.jpa.JpaDocumentInstanceRepository;

@Repository
public class DocumentInstanceRepository {
  @Autowired
  private JpaDocumentInstanceRepository repository;

  public DocumentInstance save(DocumentInstance entity) {
    return repository.save(entity);
  }

  public DocumentInstance findOne(Long id) {
    return repository.findOne(id);
  }

  public DocumentInstance findOne(
      String workspaceOwnerName,
      String workspaceName, String documentName, int version) {
    return repository.findOne(
      workspaceOwnerName,
      workspaceName, documentName, version);
  }

  private int getNextVersion(Document document) {
    DocumentInstance lastInstance = document.getLastInstance();
    if (lastInstance == null) {
      return 1;
    }
    int lastVersion = lastInstance.getVersion();
    int nextVersion = lastVersion + 1;
    return nextVersion;
  }

  private void created(Document document, DocumentInstance createdInstance) {
    document.setLastInstance(createdInstance);
  }

  public DocumentInstance create(Document document) {
    int version = getNextVersion(document);
    DocumentInstance instance = new DocumentInstance(version);
    instance.setDocument(document);
    created(document, instance);
    return instance;
  }
  
  public long totalSizeByOwner(SocialActor owner) {
    return repository.totalSizeByOwner(owner);
  }
}
