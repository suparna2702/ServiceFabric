package com.similan.portal.view.contentspace;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import com.similan.framework.dto.fileImport.CloudImportFileDto;
import com.similan.framework.dto.fileImport.ImportFileDto;
import com.similan.portal.view.externalStorage.ExternalContentManager;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
@Slf4j
public abstract class CloudFileImportView extends BaseFileImportView {

  private static final long serialVersionUID = 1L;

  String oauthVerifier = null;

  public abstract ExternalContentManager getExternalStorageManager();

  public void handleReturnFromCloudDrive() {
    try {
      if (this.getExternalStorageManager().isOauthOthenticationInitiated() == false) {

        log.info("Returning without fetching content " + oauthVerifier);
        return;
      }

      oauthVerifier = this.getContextParam("code");
      log.info("Handling OAuth2 callback retuened code " + oauthVerifier);

      ExternalContentManager externalManager = this.getExternalStorageManager();
      externalManager.performAuthentication(oauthVerifier);

      boolean connected = externalManager.isConnected();
      log.info("Cloud drive connection status " + connected);
      if (connected) {
        this.fetchCloudDriveRootFiles();
      }

    } catch (Exception exp) {
      log.error("Cannot connect to cloud drive", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_WARN, "Status",
              "Cannot connect to "
                  + this.getExternalStorageManager().getServiceType()
                      .toString() + " due to " + exp.getMessage()));
    }
  }

  protected void createDocument(ImportFileDto file) {
    try {
      this.getExternalStorageManager().downloadFile((CloudImportFileDto) file);
      log.info("File successfully downloaded "
          + ((CloudImportFileDto) file).getDownloadedFile().getAbsolutePath());

      createDocumentFromImportFile(file);

    } catch (Exception e) {
      log.error("Unable to download file " + file.getFileName(), e);
    }
  }

  public void selectFile(String uuid) {
    if (log.isDebugEnabled()) {
      log.info("Selecting file " + uuid);
    }

    ImportFileDto selectedFile = this.fileMap.get(uuid);
    if (selectedFile != null) {
      if (log.isDebugEnabled()) {
        log.info("Selecting file with name " + selectedFile.getFileName());
      }
      selectedFile.setSelected(true);
    }
  }
  
  public void unSelectFile(String uuid) {
    if (log.isDebugEnabled()) {
      log.info("Selecting file " + uuid);
    }

    ImportFileDto selectedFile = this.fileMap.get(uuid);
    if (selectedFile != null) {
      if (log.isDebugEnabled()) {
        log.info("Selecting file with name " + selectedFile.getFileName());
      }
      selectedFile.setSelected(false);
    }
  }

  public void fetchCloudDriveRootFiles() {

    if (this.uploadFileList == null || this.uploadFileList.size() <= 0) {
      if (getExternalStorageManager().isConnected()) {

        try {
          List<CloudImportFileDto> files = getExternalStorageManager()
              .getRootFiles();
          if (files != null && files.size() > 0) {
            log.info("Number of files " + files.size());
            if (this.uploadFileList == null) {
              this.uploadFileList = new ArrayList<ImportFileDto>();
            }

            for (CloudImportFileDto file : files) {
              file.setSelected(false);
              this.uploadFileList.add(file);
              this.fileMap.put(file.getUuid(), file);
            }
          } else {
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Cannot fetch any file "
                        + this.getExternalStorageManager().getServiceType()
                            .toString()));
          }
        } catch (Exception exp) {
          log.error("Unable to get cloud drive files", exp);
          FacesContext.getCurrentInstance().addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                  "Cannot fetch from "
                      + this.getExternalStorageManager().getServiceType()
                          .toString() + " drive due to " + exp.getMessage()));
        }
      }
    }

  }

}
