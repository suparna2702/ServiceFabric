package com.similan.service.impl.collaborationworkspace;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceInvite;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceParticipation;
import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.collaborationworkspace.Task;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceInviteRepository;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceParticipationRepository;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceRepository;
import com.similan.domain.repository.collaborationworkspace.SharedDocumentRepository;
import com.similan.domain.repository.collaborationworkspace.TaskRepository;
import com.similan.domain.repository.wall.WallEntryRepository;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceParticipationDto;
import com.similan.service.api.collaborationworkspace.dto.basic.InviteDto;
import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentAndStatisticsDto;
import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.basic.TaskDto;
import com.similan.service.api.collaborationworkspace.dto.basic.TaskStatus;
import com.similan.service.api.collaborationworkspace.dto.basic.TaskType;
import com.similan.service.api.collaborationworkspace.dto.error.CollaborationWorkspaceErrorCode;
import com.similan.service.api.collaborationworkspace.dto.error.CollaborationWorkspaceException;
import com.similan.service.api.collaborationworkspace.dto.extended.CollaborationWorkspaceStatisticsDto;
import com.similan.service.api.collaborationworkspace.dto.extended.CollaborationWorkspaceWithStatisticsDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceParticipationKey;
import com.similan.service.api.collaborationworkspace.dto.key.InviteKey;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.collaborationworkspace.dto.key.TaskKey;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.extended.DocumentStatisticsDto;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.impl.Marshaller;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.document.DocumentMarshaller;

@Slf4j
@Component
public class CollaborationWorkspaceMarshaller extends Marshaller {
  @Autowired
  private CollaborationWorkspaceRepository collaborationWorkspaceRepository;
  @Autowired
  private CollaborationWorkspaceParticipationRepository collaborationWorkspaceParticipationRepository;
  @Autowired
  private SharedDocumentRepository sharedDocumentRepository;
  @Autowired
  private TaskRepository taskRepository;
  @Autowired
  private CollaborationWorkspaceInviteRepository collaborationWorkspaceInviteRepository;
  @Autowired
  private WallEntryRepository wallEntryRepository;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private DocumentMarshaller documentMarshaller;

  public CollaborationWorkspace unmarshallWorkspaceKey(
      CollaborationWorkspaceKey workspaceKey, boolean required) {
    SocialActorKey ownerKey = workspaceKey.getOwner();

    String ownerName = ownerKey.getName();
    String name = workspaceKey.getName();

    CollaborationWorkspace workspace = collaborationWorkspaceRepository
        .findOne(ownerName, name);
    if (workspace == null && required) {
      throw new CollaborationWorkspaceException(
          CollaborationWorkspaceErrorCode.WORKSPACE_NOT_FOUND, "Workspace "
              + workspaceKey + " not found.");
    }
    return workspace;
  }

  public CollaborationWorkspaceKey marshallWorkspaceKey(
      CollaborationWorkspace workspace) {
    SocialActor owner = workspace.getOwner();

    SocialActorKey ownerKey = actorMarshaller.marshallActorKey(owner);
    String name = workspace.getName();

    CollaborationWorkspaceKey key = new CollaborationWorkspaceKey(ownerKey,
        name);
    key.setId(workspace.getId());

    return key;
  }

  public CollaborationWorkspaceDto marshallWorkspace(
      CollaborationWorkspace workspace) {

    CollaborationWorkspaceKey key = marshallWorkspaceKey(workspace);
    SocialActorKey creatorKey = this.actorMarshaller.marshallActorKey(workspace
        .getCreator());
    String description = workspace.getDescription();
    String logo = workspace.getLogo();
    Date creationDate = workspace.getTimeStamp();

    int numberOfParticipants = 1;
    if (workspace.getParticipations() != null) {
      numberOfParticipants = workspace.getParticipations().size();
    }

    CollaborationWorkspaceDto workspaceDto = new CollaborationWorkspaceDto(key,
        description, logo, creationDate, workspace.getPartnerWorkspace(),
        creatorKey, numberOfParticipants);

    if (workspace.getConfig() != null) {
      workspaceDto.setShowActivity(workspace.getConfig().getShowActivity());
      workspaceDto.setShowParticipants(workspace.getConfig()
          .getShowParticipants());
    }

    return workspaceDto;
  }

  public CollaborationWorkspaceStatisticsDto marshallWorkspaceStatistics(
      CollaborationWorkspace workspace) {

    CollaborationWorkspaceKey key = this.marshallWorkspaceKey(workspace);

    Long documentViewCount = this.wallEntryRepository.findWallEntryCount(
        EntityType.COLLABORATION_WORKSPACE,
        WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_VIEWED,
        workspace.getId());

    Long documentDownloadCount = this.wallEntryRepository.findWallEntryCount(
        EntityType.COLLABORATION_WORKSPACE,
        WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_DOWNLOADED,
        workspace.getId());

    Long documentCommentCount = this.wallEntryRepository.findWallEntryCount(
        EntityType.COLLABORATION_WORKSPACE, WallEntryType.COMMENT_POSTED,
        workspace.getId());

    Long workspaceAccessCount = this.wallEntryRepository.findWallEntryCount(
        EntityType.COLLABORATION_WORKSPACE,
        WallEntryType.COLLABORATION_WORKSPACE_ACCESSED, workspace.getId());

    Long documentSharedCount = this.wallEntryRepository.findWallEntryCount(
        EntityType.COLLABORATION_WORKSPACE,
        WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_SHARED,
        workspace.getId());

    CollaborationWorkspaceStatisticsDto workspaceStatDto = new CollaborationWorkspaceStatisticsDto(
        key, documentViewCount, documentDownloadCount, documentCommentCount,
        workspaceAccessCount, documentSharedCount);

    return workspaceStatDto;
  }

  public List<CollaborationWorkspaceWithStatisticsDto> marshallWorkspacesWithStatistics(
      List<CollaborationWorkspace> workspaces) {
    List<CollaborationWorkspaceWithStatisticsDto> ret = new ArrayList<CollaborationWorkspaceWithStatisticsDto>();

    for (CollaborationWorkspace workspace : workspaces) {
      CollaborationWorkspaceDto workspaceDto = marshallWorkspace(workspace);
      CollaborationWorkspaceStatisticsDto workspaceStatisticsDto = marshallWorkspaceStatistics(workspace);
      CollaborationWorkspaceWithStatisticsDto workspaceWithStatistics = new CollaborationWorkspaceWithStatisticsDto(
          workspaceDto.getKey(), workspaceDto, workspaceStatisticsDto);
      ret.add(workspaceWithStatistics);
    }

    return ret;
  }

  public CollaborationWorkspaceWithStatisticsDto marshallWorkspaceWithStatistics(
      CollaborationWorkspace workspace) {
    CollaborationWorkspaceDto workspaceDto = marshallWorkspace(workspace);
    CollaborationWorkspaceStatisticsDto workspaceStatisticsDto = marshallWorkspaceStatistics(workspace);
    CollaborationWorkspaceWithStatisticsDto workspaceWithStatistics = new CollaborationWorkspaceWithStatisticsDto(
        workspaceDto.getKey(), workspaceDto, workspaceStatisticsDto);

    return workspaceWithStatistics;

  }

  public List<CollaborationWorkspaceDto> marshallWorkspaces(
      List<CollaborationWorkspace> workspaces) {
    List<CollaborationWorkspaceDto> workspaceDtos = new ArrayList<CollaborationWorkspaceDto>(
        workspaces.size());
    for (CollaborationWorkspace workspace : workspaces) {
      CollaborationWorkspaceDto workspaceDto = marshallWorkspace(workspace);
      workspaceDtos.add(workspaceDto);
    }
    return workspaceDtos;
  }

  public List<CollaborationWorkspaceParticipationDto> marshallParticipations(
      List<CollaborationWorkspaceParticipation> participations) {
    List<CollaborationWorkspaceParticipationDto> participationDtos = new ArrayList<CollaborationWorkspaceParticipationDto>(
        participations.size());
    for (CollaborationWorkspaceParticipation participation : participations) {
      CollaborationWorkspaceParticipationDto participationDto = marshallParticipation(participation);
      participationDtos.add(participationDto);
    }
    return participationDtos;
  }

  public CollaborationWorkspaceParticipationDto marshallParticipation(
      CollaborationWorkspaceParticipation participation) {

    CollaborationWorkspaceParticipationKey key = marshallParticipationKey(participation);
    Date joinDate = participation.getJoinDate();

    CollaborationWorkspaceParticipationDto participationDto = new CollaborationWorkspaceParticipationDto(
        key, joinDate);

    return participationDto;
  }

  public CollaborationWorkspaceParticipationKey marshallParticipationKey(
      CollaborationWorkspaceParticipation participation) {
    CollaborationWorkspace workspace = participation.getWorkspace();
    SocialActor participant = participation.getParticipant();

    CollaborationWorkspaceKey workspaceKey = marshallWorkspaceKey(workspace);
    SocialActorKey participantKey = actorMarshaller
        .marshallActorKey(participant);

    CollaborationWorkspaceParticipationKey key = new CollaborationWorkspaceParticipationKey(
        workspaceKey, participantKey);
    key.setId(participation.getId());

    return key;
  }

  public CollaborationWorkspaceParticipation unmarshallParticipationKey(
      CollaborationWorkspaceParticipationKey participationKey, boolean required) {
    CollaborationWorkspaceKey workspaceKey = participationKey.getWorkspace();
    SocialActorKey participantKey = participationKey.getParticipant();

    String workspaceOwnerName = workspaceKey.getOwner().getName();
    String workspaceName = workspaceKey.getName();
    String participantName = participantKey.getName();

    CollaborationWorkspaceParticipation participation = collaborationWorkspaceParticipationRepository
        .findOne(workspaceOwnerName, workspaceName, participantName);
    if (participation == null && required) {
      throw new CollaborationWorkspaceException(
          CollaborationWorkspaceErrorCode.PARTICIPATION_NOT_FOUND,
          "Participation " + participationKey + " not found.");
    }
    return participation;
  }

  public List<SharedDocumentDto> marshallSharedDocuments(
      List<SharedDocument> sharedDocuments) {
    List<SharedDocumentDto> sharedDocumentDtos = new ArrayList<SharedDocumentDto>(
        sharedDocuments.size());
    for (SharedDocument sharedDocument : sharedDocuments) {
      SharedDocumentDto sharedDocumentDto = marshallSharedDocument(sharedDocument);
      sharedDocumentDtos.add(sharedDocumentDto);
    }
    return sharedDocumentDtos;
  }

  public SharedDocumentDto marshallSharedDocument(SharedDocument sharedDocument) {
    SocialActor sharer = sharedDocument.getCreator();

    SharedDocumentKey key = marshallSharedDocumentKey(sharedDocument);
    SocialActorKey sharerKey = actorMarshaller.marshallActorKey(sharer);
    Date creationDate = sharedDocument.getCreationDate();

    SharedDocumentDto sharedDocumentDto = new SharedDocumentDto(key, sharerKey,
        creationDate);
    sharedDocumentDto.setUnshared(sharedDocument.getUnshared());
    sharedDocumentDto.setDescription(sharedDocument.getDocument()
        .getDescription());

    return sharedDocumentDto;
  }

  public List<SharedDocumentAndStatisticsDto> marshallSharedDocumentsAndStatistics(
      List<SharedDocument> sharedDocuments) {
    List<SharedDocumentAndStatisticsDto> sharedDocumentDtos = new ArrayList<SharedDocumentAndStatisticsDto>(
        sharedDocuments.size());

    for (SharedDocument sharedDocument : sharedDocuments) {
      SharedDocumentAndStatisticsDto sharedDocumentDto = marshallSharedDocumentAndStatistics(sharedDocument);
      sharedDocumentDtos.add(sharedDocumentDto);
    }

    return sharedDocumentDtos;
  }

  public SharedDocumentAndStatisticsDto marshallSharedDocumentAndStatistics(
      SharedDocument sharedDocument) {
    Long downloadCount = this.wallEntryRepository
        .findDocumentDownloadedWallEntryCount(sharedDocument.getId(),
            sharedDocument.getWorkspace().getId());
    Long viewCount = this.wallEntryRepository.findDocumentViewedWallEntryCount(
        sharedDocument.getId(), sharedDocument.getWorkspace().getId());
    Long commentCount = this.wallEntryRepository.findCommentWallEntryCount(
        sharedDocument.getId(), sharedDocument.getWorkspace().getId());

    DocumentKey key = documentMarshaller.marshallDocumentKey(sharedDocument
        .getDocument());
    SharedDocumentDto sharedDocumentDto = this
        .marshallSharedDocument(sharedDocument);
    DocumentStatisticsDto docStats = new DocumentStatisticsDto(key, viewCount,
        commentCount, downloadCount);
    SharedDocumentAndStatisticsDto docWithStats = new SharedDocumentAndStatisticsDto(
        sharedDocumentDto.getKey(), sharedDocumentDto, docStats, sharedDocument
            .getDocument().getLastInstance().getVersion());
    return docWithStats;
  }

  public SharedDocumentKey marshallSharedDocumentKey(
      SharedDocument sharedDocument) {
    CollaborationWorkspace workspace = sharedDocument.getWorkspace();
    Document document = sharedDocument.getDocument();

    CollaborationWorkspaceKey workspaceKey = marshallWorkspaceKey(workspace);
    DocumentKey documentKey = documentMarshaller.marshallDocumentKey(document);

    SharedDocumentKey key = new SharedDocumentKey(workspaceKey, documentKey);
    key.setId(sharedDocument.getId());

    return key;
  }

  public SharedDocument unmarshallSharedDocumentKey(
      SharedDocumentKey sharedDocumentKey, boolean required) {
    CollaborationWorkspaceKey workspaceKey = sharedDocumentKey.getWorkspace();
    DocumentKey documentKey = sharedDocumentKey.getDocument();
    ManagementWorkspaceKey documentWorkspaceKey = documentKey.getWorkspace();

    String workspaceOwnerName = workspaceKey.getOwner().getName();
    String workspaceName = workspaceKey.getName();
    String documentWorkspaceName = documentWorkspaceKey.getName();
    String documentName = documentKey.getName();

    log.info("Collab work space owner " + workspaceOwnerName
        + " workspace name " + workspaceName + " management workspace owner "
        + documentWorkspaceName + " management workspace "
        + documentWorkspaceName);

    SharedDocument sharedDocument = sharedDocumentRepository.findOne(
        workspaceOwnerName, workspaceName, documentWorkspaceName, documentName);
    if (sharedDocument == null && required) {
      throw new CollaborationWorkspaceException(
          CollaborationWorkspaceErrorCode.SHARED_DOCUMENT_NOT_FOUND,
          "Shared document " + sharedDocumentKey + " not found.");
    }
    return sharedDocument;
  }

  public List<TaskDto> marshallTasks(List<Task> tasks) {
    List<TaskDto> taskDtos = new ArrayList<TaskDto>(tasks.size());
    for (Task task : tasks) {
      TaskDto taskDto = marshallTask(task);
      taskDtos.add(taskDto);
    }
    return taskDtos;
  }

  public TaskDto marshallTask(Task task) {

    SocialActor creator = task.getCreator();
    SocialActor assignee = task.getAssignee();

    TaskKey key = marshallTaskKey(task);
    Date creationDate = task.getCreationDate();
    String description = task.getDescription();
    SocialActorKey creatorKey = actorMarshaller.marshallActorKey(creator);
    SocialActorKey assigneeKey = assignee == null ? null : actorMarshaller
        .marshallActorKey(assignee);
    Date dueDate = task.getDueDate();
    TaskType type = task.getType();
    TaskStatus status = task.getStatus();

    TaskDto taskDto = new TaskDto(key, creationDate, description, creatorKey,
        assigneeKey, dueDate, type, status);

    return taskDto;
  }

  public TaskKey marshallTaskKey(Task task) {
    CollaborationWorkspace workspace = task.getWorkspace();

    CollaborationWorkspaceKey workspaceKey = marshallWorkspaceKey(workspace);
    String name = task.getName();

    TaskKey key = new TaskKey(workspaceKey, name);
    return key;
  }

  public Task unmarshallTaskKey(TaskKey taskKey, boolean required) {
    CollaborationWorkspaceKey workspaceKey = taskKey.getWorkspace();

    String workspaceOwnerName = workspaceKey.getOwner().getName();
    String workspaceName = workspaceKey.getName();
    String name = taskKey.getName();

    Task task = taskRepository.findOne(workspaceOwnerName, workspaceName, name);
    if (task == null && required) {
      throw new CollaborationWorkspaceException(
          CollaborationWorkspaceErrorCode.TASK_NOT_FOUND, "Task " + taskKey
              + " not found.");
    }
    return task;
  }

  public List<InviteDto> marshallInvites(
      List<CollaborationWorkspaceInvite> invites) {
    List<InviteDto> inviteDtos = new ArrayList<InviteDto>(invites.size());
    for (CollaborationWorkspaceInvite invite : invites) {
      InviteDto inviteDto = marshallInvite(invite);
      inviteDtos.add(inviteDto);
    }
    return inviteDtos;
  }

  public InviteDto marshallInvite(CollaborationWorkspaceInvite invite) {

    SocialActor inviter = invite.getInviter();

    InviteKey key = marshallInviteKey(invite);
    SocialActorKey inviterKey = actorMarshaller.marshallActorKey(inviter);

    InviteDto inviteDto = new InviteDto(key, inviterKey);

    return inviteDto;
  }

  public InviteKey marshallInviteKey(CollaborationWorkspaceInvite invite) {
    CollaborationWorkspace workspace = invite.getWorkspace();
    SocialActor invitee = invite.getInvitee();

    CollaborationWorkspaceKey workspaceKey = marshallWorkspaceKey(workspace);
    SocialActorKey inviteeKey = actorMarshaller.marshallActorKey(invitee);

    InviteKey key = new InviteKey(workspaceKey, inviteeKey);
    key.setId(invite.getId());

    return key;
  }

  public CollaborationWorkspaceInvite unmarshallInviteKey(InviteKey inviteKey,
      boolean required) {
    CollaborationWorkspaceKey workspaceKey = inviteKey.getWorkspace();
    SocialActorKey inviteeKey = inviteKey.getInvitee();

    String workspaceOwnerName = workspaceKey.getOwner().getName();
    String workspaceName = workspaceKey.getName();
    String inviteeName = inviteeKey.getName();

    CollaborationWorkspaceInvite invite = collaborationWorkspaceInviteRepository
        .findOne(workspaceOwnerName, workspaceName, inviteeName);
    if (invite == null && required) {
      throw new CollaborationWorkspaceException(
          CollaborationWorkspaceErrorCode.INVITE_NOT_FOUND, "Invite "
              + inviteKey + " not found.");
    }
    return invite;
  }
}
