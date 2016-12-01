package com.similan.portal.view.partner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.crocodoc.CrocodocException;
import com.similan.portal.model.DocumentPageModel;
import com.similan.portal.view.BaseView;
import com.similan.service.api.document.dto.basic.DocumentPageDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.share.ExternalSharedKey;

@Scope("view")
@Component("externalSharedDocumentView")
@Slf4j
public class ExternalSharedDocumentView extends BaseView {

  private static final long serialVersionUID = 1L;


  private DocumentInstanceAndDocumentAndViewerElementsDto documentInstance;

  private List<DocumentPageModel> pages;

  @PostConstruct
  public void init() {

    String shredDoc = this.getContextParam("share");
    log.info("Initializing exterally shared document view for " + shredDoc);

    ExternalSharedKey sharedDocKey = new ExternalSharedKey(shredDoc);
    documentInstance = this.getCollabDocumentShareService().getForViewer(
        sharedDocKey);

    pages = new ArrayList<DocumentPageModel>();
    if (documentInstance.getPages() != null
        && documentInstance.getPages().size() > 0) {
      for (DocumentPageDto dto : documentInstance.getPages()) {
        pages.add(new DocumentPageModel(dto));
      }
    }
  }

  public DocumentInstanceAndDocumentAndViewerElementsDto getDocumentInstance() {
    return documentInstance;
  }

  public List<DocumentPageModel> getPages() {
    return pages;
  }

  public String generateCrocodocToken() throws CrocodocException {
    return this.generateCrocodocTokenReadOnly(documentInstance);
  }

  public String generateBoxToken() throws IOException {
    return this.generateBoxToken(documentInstance);
  }

  public String getViewerId() {
    String viewerId = documentInstance.getViewerId();
    log.info("Viewer id " + viewerId);

    return viewerId;
  }

}
