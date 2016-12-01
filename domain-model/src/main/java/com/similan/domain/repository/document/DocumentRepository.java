package com.similan.domain.repository.document;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.document.DocumentCategory;
import com.similan.domain.entity.document.DocumentLabel;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.repository.document.jpa.JpaDocumentRepository;

@Repository
public class DocumentRepository {
  @Autowired
  private JpaDocumentRepository repository;

  public Document save(Document entity) {
    return repository.save(entity);
  }

  public void delete(Document entity) {
    repository.delete(entity);
  }

  public Document findOne(Long id) {
    return repository.findOne(id);
  }

  public int countByManagementWorkspace(ManagementWorkspace workspace) {
    return repository.countByManagementWorkspace(workspace);
  }

  public Document findOne(String workspaceOwnerName, String workspaceName,
      String name) {
    return repository.findOne(workspaceOwnerName, workspaceName, name);
  }

  public List<Document> find(String workspaceOwnerName, String workspaceName,
      String name) {
    return repository.find(workspaceOwnerName, workspaceName, name);
  }

  public List<Document> find(ManagementWorkspace workspace, String name) {
    return repository.find(workspace, name);
  }

  public Document create(ManagementWorkspace workspace, String name,
      String description, List<DocumentLabel> labels,
      List<DocumentCategory> categories) {

    Document document = new Document(name, description);
    if (labels != null) {
      document.setLabels(labels);
    }

    if (categories != null) {
      document.setCategories(categories);
    }

    document.setWorkspace(workspace);
    return document;
  }

  public int countByOwner(SocialActor owner) {
    return repository.countByOwner(owner);
  }
}
