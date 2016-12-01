package com.similan.service.impl.managementworkspace;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.entity.managementworkspace.ManagementWorkspaceParticipation;
import com.similan.domain.repository.managementworkspace.ManagementWorkspaceRepository;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.managementworkspace.dto.basic.ManagementWorkspaceDto;
import com.similan.service.api.managementworkspace.dto.error.ContentWorkspaceErrorCode;
import com.similan.service.api.managementworkspace.dto.error.ContentWorkspaceException;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceParticipationKey;
import com.similan.service.impl.Marshaller;
import com.similan.service.impl.community.SocialActorMarshaller;

@Component
@Slf4j
public class ManagementWorkspaceMarshaller extends Marshaller {
  @Autowired
  private ManagementWorkspaceRepository managementWorkspaceRepository;
  @Autowired
  private SocialActorMarshaller actorMarshaller;

  public ManagementWorkspace unmarshallWorkspaceKey(
      ManagementWorkspaceKey workspaceKey, boolean required) {
    SocialActorKey ownerKey = workspaceKey.getOwner();

    String ownerName = ownerKey.getName();
    String name = workspaceKey.getName();

    log.info("Getting content workspace " + ownerName + " name " + name);

    ManagementWorkspace workspace = managementWorkspaceRepository.findOne(ownerName, name);
    if (workspace == null && required) {
      throw new ContentWorkspaceException(ContentWorkspaceErrorCode.WORKSPACE_NOT_FOUND, "Workspace " + workspaceKey
          + " not found.");
    }
    return workspace;
  }

  public ManagementWorkspaceKey marshallWorkspaceKey(
      ManagementWorkspace workspace) {
    SocialActor owner = workspace.getOwner();

    SocialActorKey ownerKey = actorMarshaller
        .marshallActorKey(owner);
    String name = workspace.getName();

    ManagementWorkspaceKey key = new ManagementWorkspaceKey(ownerKey, name);
    return key;
  }

  public ManagementWorkspaceParticipationKey marshallWorkspaceParticipationKey(
      ManagementWorkspaceParticipation perticipation) {
    SocialActorKey pertKey = actorMarshaller
        .marshallActorKey(perticipation.getParticipant());
    ManagementWorkspaceKey workspaceKey = this
        .marshallWorkspaceKey(perticipation.getWorkspace());
    ManagementWorkspaceParticipationKey retKey = new ManagementWorkspaceParticipationKey(
        pertKey, workspaceKey);

    return retKey;
  }

  public ManagementWorkspaceDto marshallWorkspace(ManagementWorkspace workspace) {

    ManagementWorkspaceKey key = marshallWorkspaceKey(workspace);
    ManagementWorkspaceDto workspaceDto = new ManagementWorkspaceDto(key,
        workspace.getDescription());
    workspaceDto.setCreationDate(workspace.getCreationDate());
    if (workspace.getOwner() != null) {
      ActorDto creator =
          actorMarshaller.marshallActor(workspace.getOwner());
      workspaceDto.setCreator(creator);
    }
    return workspaceDto;
  }

  public List<ManagementWorkspaceDto> marshallWorkspaces(
      List<ManagementWorkspace> workspaces) {
    List<ManagementWorkspaceDto> workspaceDtos = new ArrayList<ManagementWorkspaceDto>(
        workspaces.size());
    for (ManagementWorkspace workspace : workspaces) {
      ManagementWorkspaceDto workspaceDto = marshallWorkspace(workspace);
      workspaceDtos.add(workspaceDto);
    }
    return workspaceDtos;
  }
}
