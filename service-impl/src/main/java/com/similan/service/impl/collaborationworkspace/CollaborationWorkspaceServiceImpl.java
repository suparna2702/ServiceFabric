package com.similan.service.impl.collaborationworkspace;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceInvite;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceParticipation;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceStatus;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.process.BusinessProcessDefinition;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceInviteRepository;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceParticipationRepository;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceRepository;
import com.similan.domain.repository.process.BusinessProcessDefinitionRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.framework.workflow.api.WorkflowInitialRequest;
import com.similan.framework.workflow.api.WorkflowManager;
import com.similan.framework.workflow.api.WorkflowResponse;
import com.similan.framework.workflow.api.WorkflowResumeRequest;
import com.similan.service.api.collaborationworkspace.CollaborationWorkspaceService;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceParticipationDto;
import com.similan.service.api.collaborationworkspace.dto.basic.InviteDto;
import com.similan.service.api.collaborationworkspace.dto.error.CollaborationWorkspaceErrorCode;
import com.similan.service.api.collaborationworkspace.dto.error.CollaborationWorkspaceException;
import com.similan.service.api.collaborationworkspace.dto.extended.CollaborationWorkspaceWithStatisticsDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.InviteKey;
import com.similan.service.api.collaborationworkspace.dto.operation.NewCollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewInviteDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewInviteResponseDto;
import com.similan.service.api.collaborationworkspace.dto.operation.UpdateCollaborationWorkspaceDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.wall.WallService;
import com.similan.service.api.wall.dto.basic.LinkReferenceDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.basic.WallPostDto;
import com.similan.service.api.wall.dto.basic.collaborationworkspace.CollaborationWorkspaceAccessedType;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.WallKey;
import com.similan.service.api.wall.dto.operation.NewWallPostDto;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.wall.WallMarshaller;
import com.similan.service.internal.api.email.EmailInternalService;
import com.similan.service.internal.api.email.EmailParameterConstants;
import com.similan.service.internal.api.email.io.NewEmail;
import com.similan.service.internal.api.event.EventInternalService;
import com.similan.service.internal.api.event.io.collaborationworkspace.CollaborationWorkspaceAccessedEvent;
import com.similan.service.internal.impl.security.ExecutionContext;
import com.similan.service.internal.impl.security.component.CollaborationWorkspaceEnforcer;

@Slf4j
@Service
public class CollaborationWorkspaceServiceImpl extends ServiceImpl implements
    CollaborationWorkspaceService {
  @Autowired
  private CollaborationWorkspaceRepository collaborationWorkspaceRepository;
  @Autowired
  private CollaborationWorkspaceParticipationRepository participantRepository;
  @Autowired
  private WorkflowManager workflowManager;
  @Autowired
  private BusinessProcessDefinitionRepository businessProcessDefinitionRepository;
  @Autowired
  private CollaborationWorkspaceInviteRepository collaborationWorkspaceInviteRepository;
  @Autowired
  private EventInternalService eventInternalService;
  @Autowired
  private EmailInternalService emailMessagingService;
  @Autowired
  private CollaborationWorkspaceEnforcer collaborationWorkspaceEnforcer;
  @Autowired
  private ExecutionContext executionContext;
  @Autowired
  private CollaborationWorkspaceParticipationRepository collaborationWorkspaceParticipationRepository;
  @Autowired
  private CollaborationWorkspaceNotificationConfigurationFilter notificationFilter;
  @Autowired
  private WallService wallService;
  @Autowired
  private PlatformCommonSettings commonSettings;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private CollaborationWorkspaceMarshaller collaborationWorkspaceMarshaller;
  @Autowired
  private WallMarshaller wallMarshaller;

  protected CollaborationWorkspaceServiceImpl() {
    collaborationWorkspaceRepository = null;
    participantRepository = null;
    workflowManager = null;
    businessProcessDefinitionRepository = null;
    collaborationWorkspaceInviteRepository = null;
    eventInternalService = null;
    emailMessagingService = null;
  }

  private Boolean isWorkspaceExist(CollaborationWorkspaceKey workspaceKey) {
    CollaborationWorkspace workspace = this.collaborationWorkspaceRepository
        .findOne(workspaceKey.getOwner().getName(), workspaceKey.getName());
    if (workspace != null) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  @Transactional
  public CollaborationWorkspaceDto create(
      CollaborationWorkspaceKey workspaceKey,
      NewCollaborationWorkspaceDto newWorkspaceDto) {
    if (newWorkspaceDto == null || workspaceKey.getOwner() == null) {
      throw new IllegalArgumentException("Cannot be null ");
    }

    /* check whether workspace with same name exists */
    if (isWorkspaceExist(workspaceKey)) {
      throw new CollaborationWorkspaceException(
          CollaborationWorkspaceErrorCode.DUPLICATE_WORKSPACE_NAME,
          "Workspace already exists by the name " + workspaceKey.getName()
              + " for owner " + workspaceKey.getOwner().getName());
    }

    SocialActorKey ownerKey = workspaceKey.getOwner();

    String name = workspaceKey.getName();
    SocialOrganization owner = (SocialOrganization) actorMarshaller
        .unmarshallActorKey(ownerKey, true);

    String description = newWorkspaceDto.getDescription();
    CollaborationWorkspaceStatus status = CollaborationWorkspaceStatus.OPEN;

    collaborationWorkspaceEnforcer.create(owner).checkAllowed();
    SocialActor creator = executionContext.invoker();

    CollaborationWorkspace workspace = this.collaborationWorkspaceRepository
        .create(owner, name, creator, description, status);
    workspace.setPartnerWorkspace(newWorkspaceDto.getPartnerWorkspace());
    collaborationWorkspaceRepository.save(workspace);

    /* creator also is a participant */
    CollaborationWorkspaceParticipation participation = this.participantRepository
        .create(workspace, creator, new Date());
    if (workspace.getParticipations() == null)
      workspace
          .setParticipations(new ArrayList<CollaborationWorkspaceParticipation>());
    workspace.getParticipations().add(participation);
    participantRepository.save(participation);

    CollaborationWorkspaceDto workspaceDto = collaborationWorkspaceMarshaller
        .marshallWorkspace(workspace);

    return workspaceDto;
  }

  @Override
  @Transactional(readOnly = true)
  public List<CollaborationWorkspaceParticipationDto> getParticipationsByParticipant(
      SocialActorKey participantKey) {
    SocialPerson participant = (SocialPerson) actorMarshaller
        .unmarshallActorKey(participantKey, true);
    collaborationWorkspaceEnforcer.getParticipationsByParticipant(participant)
        .checkAllowed();

    List<CollaborationWorkspaceParticipation> participations = this.participantRepository
        .findByParticipant(participant);

    List<CollaborationWorkspaceParticipationDto> participationDtos = collaborationWorkspaceMarshaller
        .marshallParticipations(participations);

    return participationDtos;
  }

  @Override
  @Transactional(readOnly = true)
  public List<CollaborationWorkspaceDto> getByOwner(SocialActorKey ownerKey) {
    SocialOrganization owner = (SocialOrganization) actorMarshaller
        .unmarshallActorKey(ownerKey, true);
    collaborationWorkspaceEnforcer.getByOwner(owner).checkAllowed();
    List<CollaborationWorkspace> workspaces = this.collaborationWorkspaceRepository
        .findAllByOwner(owner);

    List<CollaborationWorkspaceDto> workspaceDtos = collaborationWorkspaceMarshaller
        .marshallWorkspaces(workspaces);

    return workspaceDtos;
  }

  @Override
  @Transactional(readOnly = true)
  public List<CollaborationWorkspaceDto> getForPerticipant(
      SocialActorKey ownerKey) {
    SocialPerson participant = (SocialPerson) actorMarshaller
        .unmarshallActorKey(ownerKey, true);
    collaborationWorkspaceEnforcer.getForPerticipant(participant)
        .checkAllowed();
    List<CollaborationWorkspaceParticipation> participations = this.participantRepository
        .findByParticipant(participant);
    List<CollaborationWorkspaceDto> retWorkspace = new ArrayList<CollaborationWorkspaceDto>();
    for (CollaborationWorkspaceParticipation participantion : participations) {
      CollaborationWorkspace workspace = participantion.getWorkspace();
      CollaborationWorkspaceDto workspaceDto = collaborationWorkspaceMarshaller
          .marshallWorkspace(workspace);
      retWorkspace.add(workspaceDto);
    }

    return retWorkspace;
  }

  @Override
  @Transactional(readOnly = true)
  public CollaborationWorkspaceDto get(CollaborationWorkspaceKey workspaceKey) {
    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);
    collaborationWorkspaceEnforcer.get(workspace).checkAllowed();
    CollaborationWorkspaceDto workspaceDto = collaborationWorkspaceMarshaller
        .marshallWorkspace(workspace);
    return workspaceDto;
  }

  @Override
  @Transactional(readOnly = true)
  public CollaborationWorkspaceDto getDetail(
      CollaborationWorkspaceKey workspaceKey) {

    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);
    collaborationWorkspaceEnforcer.getDetail(workspace).checkAllowed();
    long accessedWorkspace = workspace.getId();
    SocialActor actor = executionContext.invoker();
    long accessor = actor.getId();

    eventInternalService
        .fire(new CollaborationWorkspaceAccessedEvent(accessedWorkspace,
            accessor, CollaborationWorkspaceAccessedType.VIEWED));

    /*
     * Map<String, Object> parameters = new HashMap<String, Object>();
     * parameters.put("workspaceName", workspace.getName());
     * parameters.put("viewerName", actor.getDisplayName());
     * 
     * List<SocialActor> participants = new ArrayList<SocialActor>();
     * List<CollaborationWorkspaceParticipation> participations = workspace
     * .getParticipations();
     * 
     * for (CollaborationWorkspaceParticipation participation : participations)
     * { participants.add(participation.getParticipant()); }
     * 
     * NewEmail newEmail = new NewEmail(actor, participants,
     * "collabSpaceViewed.vm", parameters);
     * this.emailMessagingService.send(newEmail);
     */

    return this.get(workspaceKey);
  }

  @Override
  @Transactional
  public List<InviteDto> invite(NewInviteDto newIinvite) {

    CollaborationWorkspaceKey workspaceKey = newIinvite.getWorkspace();
    List<SocialActorKey> inviteeKeys = newIinvite.getInvitees();

    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);
    collaborationWorkspaceEnforcer.invite(workspace).checkAllowed();
    SocialActor inviter = executionContext.invoker();

    List<CollaborationWorkspaceInvite> invites = new ArrayList<>(
        inviteeKeys.size());
    for (SocialActorKey inviteeKey : inviteeKeys) {
      SocialActor invitee = actorMarshaller
          .unmarshallActorKey(inviteeKey, true);
      CollaborationWorkspaceInvite invite = this.inviteParticipant(workspace,
          inviter, invitee);
      invites.add(invite);
    }
    List<InviteDto> InviteDtos = collaborationWorkspaceMarshaller
        .marshallInvites(invites);
    return InviteDtos;
  }

  private CollaborationWorkspaceInvite inviteParticipant(
      CollaborationWorkspace workspace, SocialActor inviter, SocialActor invitee) {

    // check whether a pending invite exists
    CollaborationWorkspaceInvite existingInvite = collaborationWorkspaceInviteRepository
        .findOne(workspace.getOwner().getName(), workspace.getName(),
            invitee.getName());

    if (existingInvite != null) {
      throw new CollaborationWorkspaceException(
          CollaborationWorkspaceErrorCode.PARTICIPANT_ALREADY_INVITED,
          "A pending invite exists for the same invitee");
    }

    CollaborationWorkspaceInvite invite = collaborationWorkspaceInviteRepository
        .create(workspace, inviter, invitee);
    collaborationWorkspaceInviteRepository.save(invite);

    BusinessProcessDefinition inviteProcess = this.businessProcessDefinitionRepository
        .findByName("collaborationWorkspaceInvitation");

    WorkflowInitialRequest initialRequest = new WorkflowInitialRequest();
    initialRequest.setFlowDefinitionName(inviteProcess.getName());

    /* set the parameters */
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(
        ProcessContextParameterConstants.COLLAB_WORKSPACE_INVITE_REQUEST_ID,
        invite.getId());
    parameters.put(ProcessContextParameterConstants.COLLAB_WORKSPACE_ID,
        workspace.getId());
    parameters.put(
        ProcessContextParameterConstants.COLLAB_WORKSPACE_INVITEE_ID,
        invitee.getId());
    parameters.put(
        ProcessContextParameterConstants.COLLAB_WORKSPACE_INVITER_ID,
        inviter.getId());

    /* set the parameters */
    initialRequest.setExecutionParameters(parameters);

    /* execute the workflow */
    WorkflowResponse response = this.workflowManager.initiate(initialRequest);
    invite.setProcessId(response.getProcessInstanceId());
    return invite;
  }

  @Override
  @Transactional(readOnly = true)
  public List<CollaborationWorkspaceParticipationDto> getParticipations(
      CollaborationWorkspaceKey workspaceKey) {

    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);
    collaborationWorkspaceEnforcer.getParticipations(workspace).checkAllowed();

    List<CollaborationWorkspaceParticipation> participations = workspace
        .getParticipations();

    List<CollaborationWorkspaceParticipationDto> participationDtos = collaborationWorkspaceMarshaller
        .marshallParticipations(participations);
    return participationDtos;
  }

  @Override
  @Transactional
  public void respondToInvite(InviteKey inviteKey,
      NewInviteResponseDto newResponseDto) {
    CollaborationWorkspaceInvite invite = collaborationWorkspaceMarshaller
        .unmarshallInviteKey(inviteKey, true);
    // collaborationWorkspaceEnforcer.respondToInvite(invite).checkAllowed();

    /* set the parameters */
    Map<String, Object> parameters = new HashMap<String, Object>();
    if (newResponseDto.getAccepted()) {
      parameters
          .put(ProcessContextParameterConstants.ACTION_RESULT, "accepted");
    } else {
      parameters
          .put(ProcessContextParameterConstants.ACTION_RESULT, "rejected");
    }

    parameters.put(
        ProcessContextParameterConstants.COLLAB_WORKSPACE_INVITE_RESULT,
        new Boolean(newResponseDto.getAccepted()));
    parameters.put(ProcessContextParameterConstants.COLLAB_WORKSPACE_ID, invite
        .getWorkspace().getId());
    parameters.put(
        ProcessContextParameterConstants.COLLAB_WORKSPACE_INVITEE_ID, invite
            .getInvitee().getId());

    parameters.put(ProcessContextParameterConstants.PROCESS_INSTANCE_ID,
        invite.getProcessId());

    WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
    resumeRequest.setExecutionParameters(parameters);
    resumeRequest.setWorkflowInstanceId(invite.getProcessId());
    this.workflowManager.resume(resumeRequest);
  }

  @Override
  @Transactional
  public List<ActorDto> getParticipants(CollaborationWorkspaceKey workspaceKey) {

    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);
    collaborationWorkspaceEnforcer.getParticipants(workspace).checkAllowed();

    List<CollaborationWorkspaceParticipation> participations = workspace
        .getParticipations();

    List<ActorDto> retSocialActorList = new ArrayList<ActorDto>();
    for (CollaborationWorkspaceParticipation participant : participations) {
      ActorDto socialActor = actorMarshaller.marshallActor(participant
          .getParticipant());
      retSocialActorList.add(socialActor);
    }

    return retSocialActorList;
  }

  @Override
  @Transactional
  public CollaborationWorkspaceDto update(
      CollaborationWorkspaceKey workspaceKey,
      UpdateCollaborationWorkspaceDto updateDto) {

    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);

    collaborationWorkspaceEnforcer.update(workspace).checkAllowed();
    SocialActor editorActor = executionContext.invoker();

    workspace.setLogo(updateDto.getLogo());
    workspace.setDescription(updateDto.getDescription());
    workspace.getConfig().setShowActivity(updateDto.isShowActivities());
    workspace.getConfig().setShowParticipants(updateDto.isShowParticipants());
    this.collaborationWorkspaceRepository.save(workspace);

    CollaborationWorkspaceDto workspaceDto = collaborationWorkspaceMarshaller
        .marshallWorkspace(workspace);

    eventInternalService.fire(new CollaborationWorkspaceAccessedEvent(workspace
        .getId(), editorActor.getId(),
        CollaborationWorkspaceAccessedType.EDITED));

    List<SocialActor> participants = this.notificationFilter.getParticipants(
        workspace, workspace.getCreator(),
        WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_UPDATED);

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(EmailParameterConstants.WORKSPACE_NAME, workspace.getName());
    parameters.put(EmailParameterConstants.WORKSPACE_EDITOR_NAME,
        editorActor.getDisplayName());

    String logoUrl = commonSettings.getPortalApplcationUrl().getValue()
        + "spring/assets" + workspace.getOwner().getImage();
    parameters.put(EmailParameterConstants.BUSINESS_LOGO, logoUrl);

    NewEmail newEmail = new NewEmail(editorActor, participants,
        "collabSpaceEdited.vm", parameters);
    this.emailMessagingService.send(newEmail);

    return workspaceDto;
  }

  @Override
  @Transactional
  public CollaborationWorkspaceWithStatisticsDto getWithStatistics(
      CollaborationWorkspaceKey workspaceKey) {
    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);
    collaborationWorkspaceEnforcer.getWithStatistics(workspace).checkAllowed();
    CollaborationWorkspaceWithStatisticsDto workspaceWithDto = this.collaborationWorkspaceMarshaller
        .marshallWorkspaceWithStatistics(workspace);
    return workspaceWithDto;
  }

  @Override
  @Transactional
  public void addParticipantInPartnerWorkspace(NewInviteDto newInvite) {

    CollaborationWorkspaceKey workspaceKey = newInvite.getWorkspace();
    List<SocialActorKey> inviteeKeys = newInvite.getInvitees();

    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);

    List<SocialActor> invites = new ArrayList<>(inviteeKeys.size());
    for (SocialActorKey inviteeKey : inviteeKeys) {
      SocialActor invitee = actorMarshaller
          .unmarshallActorKey(inviteeKey, true);
      invites.add(invitee);
    }

    for (SocialActor invitee : invites) {
      CollaborationWorkspaceParticipation collaborationWorkspaceParticipation = collaborationWorkspaceParticipationRepository
          .create(workspace, invitee, new Date());
      collaborationWorkspaceParticipationRepository
          .save(collaborationWorkspaceParticipation);

    }
  }

  @Override
  @Transactional
  public SocialActorKey getCreator(CollaborationWorkspaceKey workspaceKey) {
    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);

    SocialActor creator = workspace.getCreator();
    return actorMarshaller.marshallActorKey(creator);
  }

  @Override
  @Transactional
  public List<WallEntryDto<CollaborationWorkspaceKey>> getWallEntries(
      CollaborationWorkspaceKey workspaceKey, SocialActorKey actorKey) {

    log.info("Fetching wall entries for workspace " + workspaceKey
        + " for member " + actorKey);

    /* get the wall */
    WallKey<CollaborationWorkspaceKey> wallKey = new WallKey<CollaborationWorkspaceKey>(
        workspaceKey);

    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);
    List<WallEntryDto<CollaborationWorkspaceKey>> retEntries = null;
    SocialActor actor = actorMarshaller.unmarshallActorKey(actorKey, true);

    if (workspace.getConfig().getShowActivity() != true
        && workspace.getCreator().getId() != actor.getId()) {

      List<SocialActorKey> initiators = new ArrayList<SocialActorKey>();
      initiators.add(new SocialActorKey(workspace.getCreator().getName()));
      initiators.add(actorKey);
      retEntries = this.wallService.getLatest(initiators, wallKey);

    } else {
      retEntries = this.wallService.getLatest(wallKey);
    }

    return retEntries;

  }

  @Override
  @Transactional
  public List<LinkReferenceDto> getAllExternalLinkReferencePosts(
      CollaborationWorkspaceKey workspaceKey, SocialActorKey actorKey) {

    /* get the wall */
    WallKey<CollaborationWorkspaceKey> wallKey = new WallKey<CollaborationWorkspaceKey>(
        workspaceKey);

    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);
    List<WallEntryDto<CollaborationWorkspaceKey>> retEntries = null;
    SocialActor actor = actorMarshaller.unmarshallActorKey(actorKey, true);

    if (workspace.getConfig().getShowActivity() != true
        && workspace.getCreator().getId() != actor.getId()) {

      List<SocialActorKey> initiators = new ArrayList<SocialActorKey>();
      initiators.add(new SocialActorKey(workspace.getCreator().getName()));
      initiators.add(actorKey);
      retEntries = this.wallService.getAllExternalLinkReferencePosts(
          initiators, wallKey);

    } else {
      retEntries = this.wallService.getAllExternalLinkReferencePosts(null,
          wallKey);
    }

    // extract the link references only and return
    List<LinkReferenceDto> retLinkReference = new ArrayList<LinkReferenceDto>();
    if (retEntries != null) {
      for (WallEntryDto<CollaborationWorkspaceKey> wallEntry : retEntries) {
        if (wallEntry instanceof WallPostDto) {
          retLinkReference.add(((WallPostDto) wallEntry).getLink());
        }
      }
    }
    return retLinkReference;
  }

  /**
   * 
   * @param wall
   * @param author
   * @param entry
   * @throws URISyntaxException
   */
  private void sendEmail(Wall wall, SocialActor author)
      throws URISyntaxException {

    CollaborationWorkspace workspace = collaborationWorkspaceRepository
        .findOne(wall.getContainer().getId());

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("workspaceName", workspace.getName());
    String hostUrl = commonSettings.getPortalApplcationUrl().getValue();

    URI uri = new URIBuilder(hostUrl + "collabspace/workspaceDetail.xhtml")
        .setParameter("wsname", workspace.getName())
        .setParameter("owsname", workspace.getOwner().getName()).build();
    parameters.put("workspaceUrl", uri.toString());

    String logoUrl = commonSettings.getPortalApplcationUrl().getValue()
        + "spring/assets" + workspace.getOwner().getImage();
    parameters.put(EmailParameterConstants.BUSINESS_LOGO, logoUrl);

    String fullName = author.getDisplayName();
    parameters.put("author", fullName);

    List<SocialActor> participants = this.notificationFilter.getParticipants(
        workspace, author, WallEntryType.WALL_POST);

    NewEmail newEmail = new NewEmail(author, participants,
        "collabSpaceWallPost.vm", parameters);
    this.emailMessagingService.send(newEmail);
  }

  @Override
  @Transactional
  public <WallContainerKey extends IWallContainerKey> WallPostDto<WallContainerKey> post(
      WallKey<WallContainerKey> wallKey, NewWallPostDto newPost) {

    log.info("Posting on collab workspace wall " + wallKey + " the post "
        + newPost);

    WallPostDto<WallContainerKey> ret = this.wallService.post(wallKey, newPost);
    SocialActorKey authorKey = newPost.getAuthor();

    Wall wall = wallMarshaller.unmarshallWallKey(wallKey, true);
    SocialActor author = actorMarshaller.unmarshallActorKey(authorKey, true);

    try {
      this.sendEmail(wall, author);
    } catch (Exception exp) {
      log.error("Cannot send wall post email ", exp);
    }

    return ret;
  }

}
