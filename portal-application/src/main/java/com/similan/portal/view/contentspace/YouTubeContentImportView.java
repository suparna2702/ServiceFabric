package com.similan.portal.view.contentspace;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.fileImport.CloudImportFileDto;
import com.similan.portal.view.externalStorage.ExternalContentManager;
import com.similan.portal.view.externalStorage.YoutubeContentManager;

@Scope("view")
@Component("youTubeContentImportView")
@Slf4j
@Getter
@Setter
public class YouTubeContentImportView extends CloudFileImportView {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private YoutubeContentManager youTubeContentManager;

  private String sharedVideoUrl = StringUtils.EMPTY;

  @PostConstruct
  public void init() {
    this.initBase();
  }

  @Override
  public ExternalContentManager getExternalStorageManager() {
    if (youTubeContentManager == null) {

      FacesContext facesContext = FacesContext.getCurrentInstance();
      youTubeContentManager = (YoutubeContentManager) facesContext
          .getApplication().evaluateExpressionGet(facesContext,
              "#{youtubeContentManager}", YoutubeContentManager.class);

      log.info("Found YouTube content manager ");
    }
    return youTubeContentManager;
  }

  public void addSharedVideo() {
    log.info("Shared URL " + this.sharedVideoUrl);
    youTubeContentManager = (YoutubeContentManager) getExternalStorageManager();

    try {
      CloudImportFileDto file = youTubeContentManager
          .getVideoFromSharedUrl(this.sharedVideoUrl);

      if (file != null) {
        this.getUploadFileList().add(file);
      } else {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                "Cannot fetch video " + this.sharedVideoUrl
                    + " Either it does not exist or private "));
      }

      this.sharedVideoUrl = StringUtils.EMPTY;

    } catch (Exception exp) {
      log.error("Cannot fetch video information ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Cannot fetch video due to " + exp.getMessage()));
    }

  }

}
