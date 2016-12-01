package com.similan.portal.view.partner;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.crocodoc.CrocodocException;
import com.similan.portal.view.BaseView;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.share.InNetworkSharedKey;

@Scope("view")
@Component("inNetworkSharedDocumentView")
@Slf4j
public class InNetworkSharedDocumentView extends BaseView {

  private static final long serialVersionUID = 1L;


  private DocumentInstanceAndDocumentAndViewerElementsDto documentInstance;

  @PostConstruct
  public void init() {

    String shredDoc = this.getContextParam("share");
    log.info("Initializing shared document view for " + shredDoc);

    InNetworkSharedKey sharedDocKey = new InNetworkSharedKey(shredDoc);
    documentInstance = this.getManagementSpaceService().getForViewer(
        sharedDocKey);
  }

  public DocumentInstanceAndDocumentAndViewerElementsDto getDocumentInstance() {
    return documentInstance;
  }

  public String generateCrocodocToken() throws CrocodocException {
    return this.generateCrocodocTokenReadOnly(getDocumentInstance());
  }

}
