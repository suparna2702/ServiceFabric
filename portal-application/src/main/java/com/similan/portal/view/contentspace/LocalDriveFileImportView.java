package com.similan.portal.view.contentspace;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.fileImport.ImportFileDto;
import com.similan.framework.dto.fileImport.ImportFileSource;
import com.similan.framework.dto.fileImport.LocalImportFileDto;

@Scope("view")
@Component("localDriveFileImportView")
@Slf4j
public class LocalDriveFileImportView extends BaseFileImportView {

  private static final long serialVersionUID = 1L;
  
  @PostConstruct
  public void init() {
    this.initBase();
  }

  protected void createDocument(ImportFileDto file) {
    createDocumentFromImportFile(file);
  }

  public void handleFileUpload(FileUploadEvent event) {
    UploadedFile uploadedFile = event.getFile();

    LocalImportFileDto file = new LocalImportFileDto();
    file.setFileName(uploadedFile.getFileName());
    file.setMimeType(uploadedFile.getContentType());
    file.setFolder(false);
    file.setSize(uploadedFile.getSize());
    file.setSource(ImportFileSource.LOCAL);
    file.setUploadedFile(uploadedFile);
    log.debug("Adding : " + file);

    uploadFileList.add((ImportFileDto) file);
    localUploadedCount++;
  }

}
