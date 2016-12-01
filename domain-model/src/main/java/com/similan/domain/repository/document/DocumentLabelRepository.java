package com.similan.domain.repository.document;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.DocumentLabel;
import com.similan.domain.repository.document.jpa.JpaDocumentLabelRepository;

@Repository
public class DocumentLabelRepository {
  @Autowired
  private JpaDocumentLabelRepository repository;

  public DocumentLabel save(DocumentLabel entity) {
    return repository.save(entity);
  }

  public DocumentLabel findOne(Long id) {
    return repository.findOne(id);
  }

  public DocumentLabel findOne(String ownerName, String name) {
    return repository.findOne(ownerName, name);
  }

  public List<DocumentLabel> findByOwnerAndParent(SocialActor owner,DocumentLabel parent) {
    return repository.findByOwnerAndParent(owner, parent);
  }

  public List<DocumentLabel> findByOwnerAndNullParent(SocialActor owner) {
    return repository.findByOwnerAndNullParent(owner);
  }
  
  public DocumentLabel create(SocialActor owner, String name,
      DocumentLabel parent) {
    DocumentLabel label = new DocumentLabel(name);
    label.setOwner(owner);
    label.setParent(parent);
    return label;
  }


}
