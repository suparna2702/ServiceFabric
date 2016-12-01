package com.similan.portal.view.contentspace;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.AttachmentBuilder;
import org.apache.tika.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.fileImport.CloudImportFileDto;
import com.similan.framework.dto.fileImport.ImportFileDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.asset.NewAssetStream;
import com.similan.service.api.asset.dto.basic.AssetMetadataDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.operation.NewDocumentDto;
import com.similan.service.api.document.dto.operation.NewExternallyManagedDocumentDto;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
@Slf4j
public abstract class BaseFileImportView extends BaseView {

  private static final long serialVersionUID = 1L;

  private static final int PROGRESS_INCREAMENT = 25;

  @Autowired(required = true)
  MemberInfoDto memberInfo;

  @Autowired(required = true)
  OrganizationDetailInfoDto orgInfo;

  protected int localUploadedCount = 0;

  protected Integer progress = 0;

  protected ManagementWorkspaceKey managementKey;

  protected SocialActorKey uploaderKey = null;

  protected AtomicBoolean uploadStart = new AtomicBoolean(false);

  protected List<ImportFileDto> uploadFileList = new ArrayList<ImportFileDto>();

  protected Map<String, ImportFileDto> fileMap = new HashMap<String, ImportFileDto>();

  public void initBase() {
    /*
     * get the necessary params to create management workspace key if not go to
     * default space
     */

    String workspaceName = this.getContextParam("msname");

    if (StringUtils.isBlank(workspaceName) == true) {
      // get it from session since it is returning from registered URI
      FacesContext context = FacesContext.getCurrentInstance();
      workspaceName = (String) context.getExternalContext().getSessionMap()
          .get("msname");
      log.info("Fetched management workspace name from session "
          + workspaceName);
      context.getExternalContext().getSessionMap().remove("msname");

    }

    managementKey = new ManagementWorkspaceKey(getOrgKey(this.orgInfo),
        workspaceName);
    log.info("Management workspace key " + managementKey);
    uploaderKey = this.getMemberKey(memberInfo);

  }

  public Integer getProgress() {
    if (uploadStart.get() == true) {
      progress = progress + (int) (Math.random() * PROGRESS_INCREAMENT);

      if (progress > 100)
        progress = 0;
    }

    return progress;
  }

  public void cancelUpload() {
    // Clear out the list
    try {
      uploadFileList.clear();
      fileMap.clear();

      FacesContext
          .getCurrentInstance()
          .getExternalContext()
          .redirect(
              FacesContext.getCurrentInstance().getExternalContext()
                  .getRequestContextPath()
                  + "/docspace/contentWorkspaceDetails.xhtml?msname="
                  + this.managementKey.getName());
    } catch (Exception exp) {
      log.error("Cannot redirect", exp);
    }
  }

  public void deleteFile(ImportFileDto file) {
    log.info("Delete File called: " + file);

    for (ImportFileDto uploadFile : uploadFileList) {
      if (uploadFile.getFileName().equalsIgnoreCase(file.getFileName())) {
        uploadFileList.remove(uploadFile);
        fileMap.remove(uploadFile.getUuid());
        break;
      }
    }
  }

  protected final class FileSorter implements Comparator<ImportFileDto> {
    public int compare(ImportFileDto o1, ImportFileDto o2) {
      int i = Boolean.compare(o2.isFolder(), o1.isFolder());
      if (i != 0)
        return i;

      return o1.getFileName().compareTo(o2.getFileName());
    }
  }

  protected void createExternallyManagedDocumentFromImportFile(
      CloudImportFileDto file) {
    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
            "Feature currently not supported"));
  }

  protected void createInternallyManagedDocumentFromImportFile(
      ImportFileDto file) {
    InputStream fis = null;
    try {
      // check whether a management workspace is available
      SocialActorKey ownerKey = this.getOrgKey(orgInfo);

      // Now create a document
      AssetMetadataDto metadata = new AssetMetadataDto(file.getMimeType(), null);
      fis = file.getContent();
      DocumentKey documentKey = new DocumentKey(managementKey,
          file.getFileName());
      NewDocumentDto newDocument = new NewDocumentDto(ownerKey,
          StringUtils.EMPTY, file.getLabels(), file.getCategories(),
          file.getFileName());

      new AttachmentBuilder().mediaType(file.getMimeType()).object(fis);
      DocumentInstanceAndDocumentDto documentInstanceDto = this
          .getManagementSpaceService().create(this.managementKey, documentKey,
              newDocument, new NewAssetStream(metadata, fis));

      this.getManagementSpaceService().upload(this.managementKey, uploaderKey,
          documentInstanceDto.getKey().getDocument());

      log.info("Upload complete for file " + documentInstanceDto);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
              "Successfully uploaded file "));

    } catch (Exception exp) {
      log.error("File upload failed due to " + exp.getMessage(), exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "File upload failed due to " + exp.getMessage()));
    } finally {
      IOUtils.closeQuietly(fis);
    }
  }

  protected void createDocumentFromImportFile(ImportFileDto file) {
    if (file.isExternallyManaged()) {
      this.createExternallyManagedDocumentFromImportFile((CloudImportFileDto) file);
    } else {
      this.createInternallyManagedDocumentFromImportFile(file);
    }
  }

  protected abstract void createDocument(ImportFileDto file);

  public void uploadSelectedFiles() {
    uploadStart.set(true);
    log.debug("uploadSelectedFiles called");

    if (uploadFileList != null) {

      // total number of file will determine each increament
      // Redirect to recent uploads page
      try {
        for (ImportFileDto file : uploadFileList) {
          if (file.isSelected()) {
            log.debug("Creating document for file " + file);
            createDocument(file);
          }
        }

        // Clear out the list
        uploadFileList.clear();
        fileMap.clear();
        uploadStart.set(false);

        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Import Complete",
                "The selected files have been uploaded."));

        String redirectUrl = this.managementWorkspaceView(
            "docspace/contentWorkspaceDetails.xhtml", this.managementKey);
        log.info("Redirect URL " + redirectUrl);

        FacesContext.getCurrentInstance().getExternalContext()
            .redirect(redirectUrl);

      } catch (Exception exp) {
        log.error("Upload failed ", exp);
        // Clear out the list
        uploadFileList.clear();
        fileMap.clear();
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                "File upload failed due to " + exp.getMessage()));
      }
    } else {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_WARN, "Message",
              "Please select a file "));
    }
  }

}
