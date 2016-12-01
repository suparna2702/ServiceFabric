package com.similan.portal.view.collabspace;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.portal.view.BaseView;
import com.similan.service.api.collaborationworkspace.dto.key.InviteKey;
import com.similan.service.api.collaborationworkspace.dto.operation.NewInviteResponseDto;

@Scope("view")
@Component("collabWorkspaceInviteNotificationCompleteView")
@Slf4j
public class CollabWorkspaceInviteNotificationCompleteView extends BaseView {

  private static final long serialVersionUID = 1L;

  private String workspaceOwnerName = StringUtils.EMPTY;
  private String workspaceName = StringUtils.EMPTY;
  private String inviteeName = StringUtils.EMPTY;

  public void acceptInvite() {

    try {
      workspaceOwnerName = this.getContextParam("wsowner");
      workspaceName = this.getContextParam("wsname");
      inviteeName = this.getContextParam("invitee");
      String result = this.getContextParam("result");
      Boolean approved = Boolean.FALSE;
      if (result.contentEquals("accept")) {
        approved = Boolean.TRUE;
      }

      InviteKey inviteKey = new InviteKey(workspaceOwnerName, workspaceName,
          inviteeName);
      NewInviteResponseDto responseDto = new NewInviteResponseDto(approved);
      collabWorkspaceService.respondToInvite(inviteKey, responseDto);
      
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
              "Invitation to workspace " + workspaceName
                  + " is successfull "));
      
    } catch (Exception exp) {

      log.error("Cannot complete invitation to workspace " + workspaceName,
          exp);

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
              "Cannot complete invitation to workspace " + workspaceName
                  + " because of " + exp.getMessage()));
    }

  }

  public String getWorkspaceName() {
    return workspaceName;
  }

  public String getWorkspaceOwnerName() {
    return workspaceOwnerName;
  }

  public String getInviteeName() {
    return inviteeName;
  }

}
