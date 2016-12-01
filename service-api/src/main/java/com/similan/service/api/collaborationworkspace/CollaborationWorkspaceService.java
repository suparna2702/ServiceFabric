package com.similan.service.api.collaborationworkspace;

import java.util.List;

import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceParticipationDto;
import com.similan.service.api.collaborationworkspace.dto.basic.InviteDto;
import com.similan.service.api.collaborationworkspace.dto.extended.CollaborationWorkspaceWithStatisticsDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.InviteKey;
import com.similan.service.api.collaborationworkspace.dto.operation.NewCollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewInviteDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewInviteResponseDto;
import com.similan.service.api.collaborationworkspace.dto.operation.UpdateCollaborationWorkspaceDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.wall.dto.basic.LinkReferenceDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.WallPostDto;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.WallKey;
import com.similan.service.api.wall.dto.operation.NewWallPostDto;

/**
 * 
 * @author suparna1108
 * 
 */
public interface CollaborationWorkspaceService {

  public <WallContainerKey extends IWallContainerKey> WallPostDto<WallContainerKey> post(
      WallKey<WallContainerKey> wallKey, NewWallPostDto newPost);

  public List<WallEntryDto<CollaborationWorkspaceKey>> getWallEntries(
      CollaborationWorkspaceKey workspaceKey, SocialActorKey actorKey);

  public List<LinkReferenceDto> getAllExternalLinkReferencePosts(
      CollaborationWorkspaceKey workspaceKey, SocialActorKey actorKey);

  public CollaborationWorkspaceDto create(
      CollaborationWorkspaceKey workspaceKey,
      NewCollaborationWorkspaceDto newWorkspaceDto);

  public CollaborationWorkspaceDto update(
      CollaborationWorkspaceKey workspaceKey,
      UpdateCollaborationWorkspaceDto updateDto);

  public List<CollaborationWorkspaceParticipationDto> getParticipationsByParticipant(
      SocialActorKey participantKey);

  public List<CollaborationWorkspaceDto> getByOwner(SocialActorKey ownerKey);

  public List<CollaborationWorkspaceDto> getForPerticipant(
      SocialActorKey participantKey);

  public CollaborationWorkspaceDto get(CollaborationWorkspaceKey workspaceKey);

  public CollaborationWorkspaceWithStatisticsDto getWithStatistics(
      CollaborationWorkspaceKey workspaceKey);

  public List<InviteDto> invite(NewInviteDto newInvite);

  public void addParticipantInPartnerWorkspace(NewInviteDto newInvite);

  public List<CollaborationWorkspaceParticipationDto> getParticipations(
      CollaborationWorkspaceKey workspaceKey);

  public List<ActorDto> getParticipants(CollaborationWorkspaceKey workspaceKey);

  public void respondToInvite(InviteKey inviteKey,
      NewInviteResponseDto newResponseDto);

  public CollaborationWorkspaceDto getDetail(
      CollaborationWorkspaceKey workspaceKey);

  public SocialActorKey getCreator(CollaborationWorkspaceKey workspaceKey);

}