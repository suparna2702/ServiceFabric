package com.similan.domain.repository.document;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.DocumentCategory;
import com.similan.domain.repository.document.jpa.JpaDocumentCategoryRepository;

@Repository
public class DocumentCategoryRepository {
  @Autowired
  private JpaDocumentCategoryRepository repository;

  public DocumentCategory save(DocumentCategory entity) {
    return repository.save(entity);
  }

  public DocumentCategory findOne(Long id) {
    return repository.findOne(id);
  }

  public DocumentCategory findOne(String ownerName, String name) {
    return repository.findOne(ownerName, name);
  }

  public List<DocumentCategory> findByOwner(SocialActor owner) {
    return repository.findByOwner(owner);
  }

  public DocumentCategory create(SocialActor owner, String name) {
    DocumentCategory category = new DocumentCategory(name);
    category.setOwner(owner);
    return category;
  }

}
