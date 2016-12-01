package com.similan.portal.view.collabspace;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;

@Scope("view")
@Component("workspaceDocumentView")
@Slf4j
public class CollabWorkspaceDocumentView extends
    CollabWorkspaceDocumentViewUtil {

  private static final long serialVersionUID = 1L;

  @Autowired
  private MemberInfoDto member;

  private CollaborationWorkspaceDto workspace = null;

  @PostConstruct
  public void init() {
    log.info("Initializing workspace document view");

    String workSpaceName = this.getContextParam("wsname");
    String workSpaceOwnerName = this.getContextParam("owsname");
    log.info("Workspace name " + workSpaceName + " owner name "
        + workSpaceOwnerName);

    if (workSpaceName != null) {
      CollaborationWorkspaceKey workspaceKey = new CollaborationWorkspaceKey(
          workSpaceOwnerName, workSpaceName);
      this.workspace = this.getCollabWorkspaceService().get(workspaceKey);

      this.getSharedDocs(workspaceKey, this.getMemberKey(member));
    }
  }

  public CollaborationWorkspaceDto getWorkspace() {
    return workspace;
  }

}
