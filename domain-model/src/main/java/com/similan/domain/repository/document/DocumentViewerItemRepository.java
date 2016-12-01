package com.similan.domain.repository.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.asset.Asset;
import com.similan.domain.entity.document.DocumentInstance;
import com.similan.domain.entity.document.DocumentViewerAsset;
import com.similan.domain.entity.document.DocumentViewerAttribute;
import com.similan.domain.entity.document.DocumentViewerItem;
import com.similan.domain.repository.document.jpa.JpaDocumentViewerItemRepository;

@Repository
public class DocumentViewerItemRepository {
  @Autowired
  private JpaDocumentViewerItemRepository repository;

  public <S extends DocumentViewerItem> S save(S entity) {
    return repository.save(entity);
  }

  public DocumentViewerItem findOne(Long id) {
    return repository.findOne(id);
  }

  public DocumentViewerItem findOne(
      String workspaceOwnerName,
      String workspaceName, String documentName,
      Integer documentInstanceVersion, String name) {
    return repository.findOne(
      workspaceOwnerName,
      workspaceName, documentName,
      documentInstanceVersion, name);
  }

  public DocumentViewerAttribute createAttribute(DocumentInstance documentInstance,
      String name, String value) {
    DocumentViewerAttribute documentViewerItem = new DocumentViewerAttribute(name, value);
    documentViewerItem.setDocumentInstance(documentInstance);
    return documentViewerItem;
  }

  public DocumentViewerAsset createAsset(DocumentInstance documentInstance,
      String name, Asset asset) {
    DocumentViewerAsset documentViewerItem = new DocumentViewerAsset(name);
    documentViewerItem.setDocumentInstance(documentInstance);
    documentViewerItem.setAsset(asset);
    return documentViewerItem;
  }

}
