package com.similan.portal.view.collabspace;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.portal.view.handler.ImageDeletion;
import com.similan.portal.view.handler.ImageUpload;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.operation.UpdateCollaborationWorkspaceDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

@Scope("view")
@Component("workspaceEditView")
@Slf4j
public class CollabWorkspaceEditView extends BaseView {

  private static final long serialVersionUID = 1L;
  public static final String IMAGES_WORKSPACE_DEFAULT_LOGO = "/images/businessLogo.jpg";

  @Autowired
  private MemberInfoDto member;

  private SocialActorKey memberKey;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  private CollaborationWorkspaceDto workspace;

  private String workspaceLogo = IMAGES_WORKSPACE_DEFAULT_LOGO;

  private String workspaceName = StringUtils.EMPTY;

  private String workspaceDescription = StringUtils.EMPTY;

  private boolean workspaceLogoExists = false;

  private boolean showParticipants = true;

  private boolean showActivities = true;

  @PostConstruct
  public void init() {

    log.info("Initializing workspace edit view ");
    String workSpaceName = this.getContextParam("wsname");
    String workSpaceOwnerName = this.getContextParam("owsname");
    log.info("Workspace name " + workSpaceName + " owner name "
        + workSpaceOwnerName);

    if (workSpaceName != null) {
      CollaborationWorkspaceKey workspaceKey = new CollaborationWorkspaceKey(
          workSpaceOwnerName, workSpaceName);
      this.workspace = this.getCollabWorkspaceService().get(workspaceKey);
      this.workspaceLogo = workspace.getLogo();
      this.workspaceDescription = workspace.getDescription();
      this.showParticipants = workspace.getShowParticipants();
      this.showActivities = workspace.getShowActivity();
      this.memberKey = this.getMemberKey(member);
    }

  }

  public boolean getShowParticipants() {
    return showParticipants;
  }

  public void setShowParticipants(boolean showParticipants) {
    this.showParticipants = showParticipants;
  }

  public boolean getShowActivities() {
    return showActivities;
  }

  public void setShowActivities(boolean showActivities) {
    this.showActivities = showActivities;
  }

  public boolean isWorkspaceLogoExists() {
    return workspaceLogoExists;
  }

  public boolean isOwner() {
    boolean owner = false;

    if (this.memberKey.getName().equalsIgnoreCase(
        this.workspace.getKey().getOwner().getName())) {
      owner = true;
    }

    return owner;
  }

  public void setWorkspaceLogoExists(boolean workspaceLogoExists) {
    this.workspaceLogoExists = workspaceLogoExists;
  }

  public String getWorkspaceLogo() {
    return PhotoViewOption.ShowPhoto.effectivePhoto(
        IMAGES_WORKSPACE_DEFAULT_LOGO, workspaceLogo);
  }

  public void setWorkspaceLogo(String workspaceLogo) {
    this.workspaceLogo = workspaceLogo;
  }

  public String getWorkspaceName() {
    return workspaceName;
  }

  public void setWorkspaceName(String workspaceName) {
    this.workspaceName = workspaceName;
  }

  public String getWorkspaceDescription() {
    return workspaceDescription;
  }

  public void setWorkspaceDescription(String workspaceDescription) {
    this.workspaceDescription = workspaceDescription;
  }

  public CollaborationWorkspaceDto getWorkspace() {
    return workspace;
  }

  public void deletePhoto() {

    this.imageUploadHandler.handleImageDeletion(new ImageDeletion() {

      public void update() throws Exception {
        workspaceLogo = StringUtils.EMPTY;
        UpdateCollaborationWorkspaceDto updateDto = new UpdateCollaborationWorkspaceDto(
            workspaceLogo, workspaceDescription, showParticipants,
            showActivities);
        workspace = collabWorkspaceService.update(workspace.getKey(), updateDto);
      }

      public String getCurrentKey() {
        return workspaceLogo;
      }
    });

  }

  public String save() {
    UpdateCollaborationWorkspaceDto updateDto = new UpdateCollaborationWorkspaceDto(
        workspaceLogo, workspaceDescription, showParticipants, showActivities);
    workspace = collabWorkspaceService.update(workspace.getKey(), updateDto);

    return "result";
  }

  public void handleFileUpload(final FileUploadEvent event) {

    String result = this.imageUploadHandler
        .handleImageUpload(new ImageUpload() {

          public void update() throws Exception {
            UpdateCollaborationWorkspaceDto updateDto = new UpdateCollaborationWorkspaceDto(
                workspaceLogo, workspaceDescription, showParticipants,
                showActivities);
            workspace = collabWorkspaceService.update(workspace.getKey(),
                updateDto);
          }

          public void setImageKey(String key) {
            workspaceLogo = key;
          }

          public String getType() {
            return "workspace";
          }

          public String getId() {
            return String.valueOf(workspace.getKey().getName());
          }

          public UploadedFile getFile() {
            return event.getFile();
          }

          public String currentKey() {
            return workspace.getLogo();
          }
        });
    if (result != null) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Error uploading image", result));
    }
  }

}
