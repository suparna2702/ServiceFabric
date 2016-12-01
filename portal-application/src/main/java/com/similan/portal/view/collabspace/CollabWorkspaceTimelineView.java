package com.similan.portal.view.collabspace;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.extended.SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.key.WallKey;

@Scope("view")
@Component("collabWorkspaceTimelineView")
@Slf4j
public class CollabWorkspaceTimelineView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired
  private MemberInfoDto memberInfo;

  private SocialActorKey viewerKey;

  private List<WallEntryDto<CollaborationWorkspaceKey>> wallEntries = null;

  private CollaborationWorkspaceDto workspace = null;

  private SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto sharedDocument;

  @PostConstruct
  public void init() {
    log.info("Initialization of document timeline view ");

    String sharedDocParam = this.getContextParam("sdoc");
    log.info("Shared document id " + sharedDocParam);

    try {
      viewerKey = this.getMemberKey(memberInfo);
      Long sharedDocumentId = Long.valueOf(sharedDocParam);
      sharedDocument = this.getCollabDocumentShareService().getForViewer(
          sharedDocumentId, viewerKey);

      this.workspace = this.getCollabWorkspaceService().get(
          sharedDocument.getKey().getWorkspace());

      WallKey<CollaborationWorkspaceKey> wallKey = new WallKey<CollaborationWorkspaceKey>(
          this.workspace.getKey());
      wallEntries = this.getWallService().getWorkspaceDocumentHistory(wallKey,
          sharedDocument.getKey());

    } catch (Exception exp) {
      log.error("Cannot fetch time line events", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
              "Cannot fetch time line events due to " + exp.getMessage()));
    }

  }

  public SocialActorKey getViewerKey() {
    return viewerKey;
  }

  public SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto getSharedDocument() {
    return sharedDocument;
  }

  public CollaborationWorkspaceDto getWorkspace() {
    return workspace;
  }

  public List<WallEntryDto<CollaborationWorkspaceKey>> getWallEntries() {
    return wallEntries;
  }

}
