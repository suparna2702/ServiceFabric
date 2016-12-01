package com.similan.portal.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.model.SelectableDataModel;

@Slf4j
public class SelectableDocumentInstanceAndDocumentModel extends
    ListDataModel<DocumentInstanceAndDocumentModel> implements
    SelectableDataModel<DocumentInstanceAndDocumentModel>, Serializable {

  private static final long serialVersionUID = 1L;

  public SelectableDocumentInstanceAndDocumentModel() {

  }

  public SelectableDocumentInstanceAndDocumentModel(
      List<DocumentInstanceAndDocumentModel> instanceModel) {
    super(instanceModel);
    log.info("Creating doc instance model");
  }

  @Override
  @SuppressWarnings("unchecked")
  public DocumentInstanceAndDocumentModel getRowData(String rowKey) {
    log.info("Selectable doc key " + rowKey);
    List<DocumentInstanceAndDocumentModel> documents = (List<DocumentInstanceAndDocumentModel>) getWrappedData();

    if (documents != null) {
      log.info("Documents " + documents.size());
    }

    for (DocumentInstanceAndDocumentModel doc : documents) {
      if (doc.getData().getDocument().getKey().getName()
          .equalsIgnoreCase(rowKey))
        return doc;
    }

    return null;
  }

  @Override
  public Object getRowKey(DocumentInstanceAndDocumentModel doc) {
    return doc.getData().getDocument().getKey().getName();
  }

}
