package com.similan.portal.view.contentspace;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.managementworkspace.dto.basic.ManagementWorkspaceDto;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.managementworkspace.dto.operation.NewManagementWorkspaceDto;

@Scope("view")
@Component("documentWorkspaceListView")
@Slf4j
public class DocumentWorkspaceListView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired(required = true)
  private MemberInfoDto member = null;

  @Autowired(required = true)
  private OrganizationDetailInfoDto orgInfo = null;

  private List<ManagementWorkspaceDto> workspaceList = new ArrayList<ManagementWorkspaceDto>();

  private String newWorkspaceName;

  private String newWorkspaceDescription;

  @PostConstruct
  public void init() {
    /*
     * ManagementWorkspaceDto defaultSpace = this.getManagementSpaceService()
     * .getDefaultForOwner(this.getOrgKey(orgInfo));
     * workspaceList.add(defaultSpace);
     */
    List<ManagementWorkspaceDto> participantSpaceList = this
        .getManagementSpaceService().getForParticipant(
            this.getMemberKey(member));
    log.debug("Number of perticipant space " + participantSpaceList.size());
    workspaceList.addAll(participantSpaceList);

  }

  public List<ManagementWorkspaceDto> getWorkspaceList() {
    return workspaceList;
  }

  public String getNewWorkspaceName() {
    return newWorkspaceName;
  }

  public void setNewWorkspaceName(String newWorkspaceName) {
    this.newWorkspaceName = newWorkspaceName;
  }

  public String getNewWorkspaceDescription() {
    return newWorkspaceDescription;
  }

  public void setNewWorkspaceDescription(String newWorkspaceDescription) {
    this.newWorkspaceDescription = newWorkspaceDescription;
  }

  public void create() {

    if (StringUtils.isBlank(newWorkspaceName)) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Error",
              "Could not create content space since name is empty."));
      return;
    }

    // create the management workspace
    ManagementWorkspaceKey workspaceKey = new ManagementWorkspaceKey(
        this.getOrgKey(orgInfo), newWorkspaceName);
    NewManagementWorkspaceDto newWorkspace = new NewManagementWorkspaceDto();
    newWorkspace.setDescription(newWorkspaceDescription);

    try {
      ManagementWorkspaceDto newWorkspaceDto = this.getManagementSpaceService()
          .create(workspaceKey, newWorkspace);
      this.workspaceList.add(newWorkspaceDto);

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "Content space is created successfully"));

    } catch (Exception exp) {
      log.error("Unable to create new workspace ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Error", "Could not create content space "
                  + exp.getMessage()));

    }

    // reset them after creating the workspace
    newWorkspaceName = StringUtils.EMPTY;
    newWorkspaceDescription = StringUtils.EMPTY;

  }
  
}
