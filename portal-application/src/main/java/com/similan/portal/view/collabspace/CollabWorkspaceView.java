package com.similan.portal.view.collabspace;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.model.SelectableContact;
import com.similan.portal.view.BaseView;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceParticipationDto;
import com.similan.service.api.collaborationworkspace.dto.extended.CollaborationWorkspaceStatisticsDto;
import com.similan.service.api.collaborationworkspace.dto.extended.CollaborationWorkspaceWithStatisticsDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.operation.NewCollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewInviteDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

@Scope("request")
@Component("workspaceView")
@Slf4j
public class CollabWorkspaceView extends BaseView {

  private static final long serialVersionUID = 1L;


  @Autowired(required = true)
  private MemberInfoDto member = null;

  private SocialActorKey memberKey = null;

  @Autowired(required = true)
  private OrganizationDetailInfoDto orgInfo = null;

  private List<CollaborationWorkspaceWithStatisticsDto> workspaces = new ArrayList<CollaborationWorkspaceWithStatisticsDto>();

  private String layoutMode = LAYOUT_MODE_LIST;

  private List<SelectableContact> contacts = null;

  private String description;
  private String name;

  @PostConstruct
  public void init() {
    log.info("Workspace view initialization ");

    /*
     * we must get all the workspaces for which this member is a perticipant
     */
    this.memberKey = this.getMemberKey(member);
    List<CollaborationWorkspaceParticipationDto> participatingList = this
        .getCollabWorkspaceService().getParticipationsByParticipant(
            this.memberKey);

    for (CollaborationWorkspaceParticipationDto collabParticipation : participatingList) {
      CollaborationWorkspaceKey workspaceKey = collabParticipation.getKey()
          .getWorkspace();
      CollaborationWorkspaceWithStatisticsDto workspaceDto = this
          .getCollabWorkspaceService().getWithStatistics(workspaceKey);
      workspaces.add(workspaceDto);

    }
  }

  public SocialActorKey getMemberKey() {
    return memberKey;
  }

  public List<CollaborationWorkspaceWithStatisticsDto> getWorkspaces() {
    return workspaces;
  }

  public void setWorkspaces(
      List<CollaborationWorkspaceWithStatisticsDto> workspaces) {
    this.workspaces = workspaces;
  }

  public MemberInfoDto getMember() {
    return this.member;
  }

  public List<SelectableContact> getContacts() {
    if (contacts == null) {
      this.contacts = this.getContacts(member, orgInfo);
    }
    return contacts;
  }

  public void create() {
    log.info("Creating workspace with name {}, description {}",
        new Object[] { this.name, this.description });

    // Create the new workspace
    try {
      CollaborationWorkspaceKey workspaceKey = new CollaborationWorkspaceKey(
          getOrgKey(this.orgInfo), name);
      NewCollaborationWorkspaceDto newCollaborationWorkspaceDto = new NewCollaborationWorkspaceDto(
          description);

      CollaborationWorkspaceDto newWorkspace = collabWorkspaceService.create(
          workspaceKey, newCollaborationWorkspaceDto);
      CollaborationWorkspaceStatisticsDto stat = new CollaborationWorkspaceStatisticsDto(
          newWorkspace.getKey(), 0L, 0L, 0L, 0L, 0L);
      CollaborationWorkspaceWithStatisticsDto newWorkspaceWithStat = new CollaborationWorkspaceWithStatisticsDto(
          newWorkspace.getKey(), newWorkspace, stat);
      workspaces.add(newWorkspaceWithStat);

      // Initiate the invite process.
      List<SocialActorKey> participants = new ArrayList<SocialActorKey>();
      for (SelectableContact contact : contacts) {
        if (contact.isSelected()) {
          SocialActorKey key = this.getSocialActorService()
              .transitional_getKey(contact.getId());
          participants.add(key);
        }
      }

      NewInviteDto invite = new NewInviteDto(newWorkspace.getKey(),
          participants);
      collabWorkspaceService.invite(invite);
    } catch (Exception exp) {
      log.error("Cannot create workspace ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Cannot create workspace ", exp.getMessage()));
    }

  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description
   *          the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  public String getLayoutMode() {
    return layoutMode;
  }

  public void setLayoutMode(String layoutMode) {
    this.layoutMode = layoutMode;
  }

}
