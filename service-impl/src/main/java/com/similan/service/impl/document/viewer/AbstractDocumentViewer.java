package com.similan.service.impl.document.viewer;

public abstract class AbstractDocumentViewer implements DocumentViewer {

  private final String id;

  public AbstractDocumentViewer(String id) {
    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }

}
