package com.similan.portal.view.contentspace;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.portal.view.externalStorage.ExternalContentManager;
import com.similan.portal.view.externalStorage.GoogleDriveStorageManager;

@Scope("view")
@Component("googleDriveFileImportView")
@Slf4j
public class GoogleDriveFileImportView extends CloudFileImportView {

  private static final long serialVersionUID = 1L;

  private ExternalContentManager googleDriveConnectionManager;

  @PostConstruct
  public void init() {
    this.initBase();
  }

  @Override
  public ExternalContentManager getExternalStorageManager() {
    log.info("Returning google drive connection manager ");
    
    if (googleDriveConnectionManager == null) {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      googleDriveConnectionManager = (ExternalContentManager) facesContext
          .getApplication().evaluateExpressionGet(facesContext,
              "#{googleDriveConnectionManager}",
              GoogleDriveStorageManager.class);
    }
    return googleDriveConnectionManager;
  }

}
