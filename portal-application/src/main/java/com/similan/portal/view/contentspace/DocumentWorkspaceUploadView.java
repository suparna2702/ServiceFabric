package com.similan.portal.view.contentspace;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.AttachmentBuilder;
import org.apache.tika.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.fileImport.CloudImportFileDto;
import com.similan.framework.dto.fileImport.ImportFileDto;
import com.similan.framework.dto.fileImport.ImportFileSource;
import com.similan.framework.dto.fileImport.LocalImportFileDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.portal.view.externalStorage.DropboxStorageManager;
import com.similan.portal.view.externalStorage.ExternalContentManager;
import com.similan.portal.view.externalStorage.GoogleDriveStorageManager;
import com.similan.service.api.asset.NewAssetStream;
import com.similan.service.api.asset.dto.basic.AssetMetadataDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.operation.NewDocumentDto;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;

@Scope("view")
@Component("workspaceUploadView")
@Slf4j
public class DocumentWorkspaceUploadView extends BaseView {

  private static final String VAL_FILE_BROWSER_CLOSE = "false";
  private static final String MODE_DROPBOX = "dropbox";
  private static final int PROGRESS_INCREAMENT = 25;

  private static final long serialVersionUID = 1L;

  private DropboxStorageManager dropboxConnectionManager;
  private ExternalContentManager googleDriveConnectionManager;

  private String openBrowser = null;
  private String cloudSource = null;
  private TreeNode[] selectedFiles = null;
  private TreeNode cloudFiles = null;
  private ImportFileDto selectedUploadFile = new CloudImportFileDto();
  private List<ImportFileDto> uploadFileList = new ArrayList<ImportFileDto>();
  private int localUploadedCount = 0;

  private Integer progress = 0;

  @Autowired(required = true)
  private MemberInfoDto memberInfo;

  @Autowired(required = true)
  private OrganizationDetailInfoDto orgInfo;

  private ManagementWorkspaceKey managementKey;

  private SocialActorKey uploaderKey = null;

  private AtomicBoolean uploadStart = new AtomicBoolean(false);

  @PostConstruct
  public void init() {
    // get the necessary params to create
    // management workspace key if not go to default space
    String workspaceName = this.getContextParam("msname");

    if (StringUtils.isBlank(workspaceName) != true) {
      managementKey = new ManagementWorkspaceKey(getOrgKey(this.orgInfo),
          workspaceName);
      uploaderKey = this.getMemberKey(memberInfo);

    }

    log.info("Management workspace key " + managementKey);
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

  public ManagementWorkspaceKey getManagementKey() {
    return managementKey;
  }

  public TreeNode getCloudFiles() {
    if (MODE_DROPBOX.equals(cloudSource))
      return getDropboxFiles();
    else if ("google".equals(cloudSource))
      return getGoogleDriveFiles();
    return new DefaultTreeNode("Root", null);
  }

  public TreeNode getGoogleDriveFiles() {
    log.debug("getGDriveFiles called " + cloudFiles);

    if (cloudFiles == null) {
      if (getGoogleDriveConnectionManager().isConnected()) {
        cloudFiles = new DefaultTreeNode("Root", null);
        try {
          List<CloudImportFileDto> files = getGoogleDriveConnectionManager()
              .getRootFiles();
          if (files != null) {
            Collections.sort(files, new FileSorter());

            for (CloudImportFileDto file : files) {
              createTreeNodeForFile(file, cloudFiles);
            }
          }
        } catch (Exception e) {
          log.error("Unable to get GDrive files", e);
        }
      }
    }
    return cloudFiles;
  }

  public TreeNode getDropboxFiles() {
    log.debug("getDropboxFiles called " + cloudFiles);

    if (cloudFiles == null) {
      if (getDropboxConnectionManager().isConnected()) {
        cloudFiles = new DefaultTreeNode("Root", null);
        try {
          List<CloudImportFileDto> files = getDropboxConnectionManager()
              .getRootFiles();
          if (files != null) {
            Collections.sort(files, new FileSorter());
            for (CloudImportFileDto file : files) {
              createTreeNodeForFile(file, cloudFiles);
            }
          }
        } catch (Exception e) {
          log.error("Unable to get dropbox files", e);
        }
      }
    }
    return cloudFiles;
  }

  private TreeNode createTreeNodeForFile(ImportFileDto file, TreeNode parent) {
    TreeNode treeNode = new DefaultTreeNode(file, parent);

    if (file.isFolder()) {
      treeNode.setSelectable(false);
      ImportFileDto loadingImportFilePlaceholder = new CloudImportFileDto();
      loadingImportFilePlaceholder.setFileName("No files or folders");
      DefaultTreeNode childPlaceholder = new DefaultTreeNode(
          loadingImportFilePlaceholder, treeNode);
      childPlaceholder.setSelectable(false);
    } else {
      treeNode.setSelectable(true);
    }
    return treeNode;

  }

  public String formatDate(Date date) {
    if (date == null)
      return "";
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
    return format.format(date);
  }

  private DropboxStorageManager getDropboxConnectionManager() {
    if (dropboxConnectionManager == null) {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      dropboxConnectionManager = (DropboxStorageManager) facesContext
          .getApplication().evaluateExpressionGet(facesContext,
              "#{dropboxConnectionManager}", DropboxStorageManager.class);
    }
    return dropboxConnectionManager;
  }

  private ExternalContentManager getGoogleDriveConnectionManager() {
    if (googleDriveConnectionManager == null) {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      googleDriveConnectionManager = (ExternalContentManager) facesContext
          .getApplication().evaluateExpressionGet(facesContext,
              "#{googleDriveConnectionManager}",
              GoogleDriveStorageManager.class);
    }
    return googleDriveConnectionManager;
  }

  public void onNodeExpand(NodeExpandEvent event) {
    log.debug("onNodeExpand called");

    TreeNode parent = event.getTreeNode();
    try {
      List<CloudImportFileDto> files = null;
      if (MODE_DROPBOX.equals(cloudSource))
        files = getDropboxConnectionManager().getFilesForFolder(
            (CloudImportFileDto) parent.getData());
      else
        files = getGoogleDriveConnectionManager().getFilesForFolder(
            (CloudImportFileDto) parent.getData());

      if (parent.getChildren() != null)
        parent.getChildren().clear();
      if (files != null) {
        for (CloudImportFileDto file : files) {
          createTreeNodeForFile(file, parent);
        }
      }

    } catch (Exception e) {
      log.error("Unable to get cloud files", e);
    }
  }

  public void addSelectedFiles() {
    setOpenBrowser(VAL_FILE_BROWSER_CLOSE);
    log.debug("addSelectedFiles called " + selectedFiles);
    if (selectedFiles != null) {
      for (int selectedFilesIndex = 0; selectedFilesIndex < selectedFiles.length; selectedFilesIndex++) {
        uploadFileList.add((ImportFileDto) selectedFiles[selectedFilesIndex]
            .getData());
      }
    }
    selectedFiles = new TreeNode[0];
    cloudSource = null;
  }

  public void deleteFile(ImportFileDto file) {
    log.info("Delete File called: " + file);

    for (ImportFileDto uploadFile : uploadFileList) {
      if (uploadFile.getFileName().equalsIgnoreCase(file.getFileName())) {
        uploadFileList.remove(uploadFile);
        break;
      }
    }
  }

  public void onNodeSelected(NodeSelectEvent event) {
    // NOOP
  }

  public void onNodeUnselected(NodeUnselectEvent event) {
    // NOOP
  }

  public String getOpenBrowser() {
    return openBrowser;
  }

  public void setOpenBrowser(String openBrowser) {
    this.openBrowser = openBrowser;
  }

  public String getCloudSource() {
    return cloudSource;
  }

  public void setCloudSource(String cloudSource) {
    this.cloudSource = cloudSource;
    selectedFiles = new TreeNode[0];
    cloudFiles = null;
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

  public TreeNode[] getSelectedFiles() {
    return selectedFiles;
  }

  public void setSelectedFiles(TreeNode[] selectedNodes) {
    this.selectedFiles = selectedNodes;
  }

  public int getSelectionCount() {
    if (selectedFiles == null)
      return 0;
    else
      return selectedFiles.length;
  }

  public List<ImportFileDto> getUploadFileList() {
    return uploadFileList;
  }

  public void setUploadFileList(List<ImportFileDto> uploadFileList) {
    this.uploadFileList = uploadFileList;
  }

  public ImportFileDto getSelectedUploadFile() {
    return selectedUploadFile;
  }

  public void setSelectedUploadFile(ImportFileDto selectedUploadFile) {
    this.selectedUploadFile = selectedUploadFile;
  }

  public int getLocalUploadedCount() {
    return localUploadedCount;
  }

  public void setLocalUploadedCount(int localUploadedCount) {
    this.localUploadedCount = localUploadedCount;
  }

  public void upload() {
    this.uploadSelectedFiles();
  }

  public void uploadSelectedFiles() {
    uploadStart.set(true);
    log.debug("uploadSelectedFiles called");

    if (uploadFileList != null) {

      // total number of file will determine each increament
      // Redirect to recent uploads page
      try {
        for (ImportFileDto file : uploadFileList) {
          log.debug("Handling file " + file);

          if (file.isSelected()) {

            switch (file.getSource()) {
            case DROPBOX:
              createDropboxDocument(file);
              break;
            case GOOGLEDRIVE:
              createGoogleDocument(file);
              break;
            case LOCAL:
              createLocalDocument(file);
              break;

            }

          }
        }

        // Clear out the list
        uploadFileList.clear();
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

  private void createDropboxDocument(ImportFileDto file) {
    try {
      getDropboxConnectionManager().downloadFile((CloudImportFileDto) file);
      log.info("File successfully uploaded "
          + ((CloudImportFileDto) file).getDownloadedFile().getAbsolutePath());

      createDocumentFromImportFile(file);

    } catch (Exception e) {
      log.error("Unable to upload file " + file, e);
    }
  }

  private SocialActorKey getOrgKey() {
    return this.getSocialActorService().transitional_getKey(
        this.orgInfo.getId());
  }

  private void createDocumentFromImportFile(ImportFileDto file) {
    InputStream fis = null;
    try {
      // check whether a management workspace is available
      SocialActorKey ownerKey = this.getOrgKey();

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
          .getManagementSpaceService()
          .create(this.managementKey, documentKey, newDocument,
              new NewAssetStream(metadata, fis));

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

  private void createGoogleDocument(ImportFileDto file) {
    try {
      getGoogleDriveConnectionManager().downloadFile((CloudImportFileDto) file);
      log.info("File successfully downloaded "
          + ((CloudImportFileDto) file).getDownloadedFile().getAbsolutePath());
      createDocumentFromImportFile(file);

    } catch (Exception e) {
      log.error("Unable to download file " + file, e);
    }
  }

  private void createLocalDocument(ImportFileDto file) {
    createDocumentFromImportFile(file);
  }

  private final class FileSorter implements Comparator<ImportFileDto> {
    public int compare(ImportFileDto o1, ImportFileDto o2) {
      int i = Boolean.compare(o2.isFolder(), o1.isFolder());
      if (i != 0)
        return i;

      return o1.getFileName().compareTo(o2.getFileName());
    }
  }

}
