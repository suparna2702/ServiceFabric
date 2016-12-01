package com.similan.domain.repository.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.asset.Asset;
import com.similan.domain.entity.document.DocumentInstance;
import com.similan.domain.entity.document.DocumentPage;
import com.similan.domain.repository.document.jpa.JpaDocumentPageRepository;

@Repository
public class DocumentPageRepository {
  @Autowired
  private JpaDocumentPageRepository repository;

  public DocumentPage save(DocumentPage entity) {
    return repository.save(entity);
  }

  public DocumentPage findOne(Long id) {
    return repository.findOne(id);
  }

  public DocumentPage findOne(
      String workspaceOwnerName,
      String workspaceName, String documentName,
      int documentInstanceVersion, int number) {
    return repository.findOne(
      workspaceOwnerName,
      workspaceName, documentName,
      documentInstanceVersion, number);
  }
  
  public DocumentPage create(DocumentInstance documentInstance, int number,
      Asset pageAsset, Asset thumbnailAsset) {
    DocumentPage page = new DocumentPage(number);
    page.setDocumentInstance(documentInstance);
    documentInstance.getPages().add(page);
    page.setPageAsset(pageAsset);
    page.setThumbnailAsset(thumbnailAsset);
    return page;
  }

}
