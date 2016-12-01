package com.similan.portal.view.contentspace;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.portal.view.externalStorage.DropboxStorageManager;
import com.similan.portal.view.externalStorage.ExternalContentManager;

@Scope("view")
@Component("dropBoxFileImportView")
@Slf4j
public class DropBoxFileImportView extends CloudFileImportView {

  private static final long serialVersionUID = 1L;

  private DropboxStorageManager dropboxConnectionManager;

  @PostConstruct
  public void init() {
    this.initBase();
  }

  @Override
  public ExternalContentManager getExternalStorageManager() {
    if (dropboxConnectionManager == null) {

      FacesContext facesContext = FacesContext.getCurrentInstance();
      dropboxConnectionManager = (DropboxStorageManager) facesContext
          .getApplication().evaluateExpressionGet(facesContext,
              "#{dropboxConnectionManager}", DropboxStorageManager.class);

      log.info("Found drop box connection manager ");
    }
    return dropboxConnectionManager;
  }

}
