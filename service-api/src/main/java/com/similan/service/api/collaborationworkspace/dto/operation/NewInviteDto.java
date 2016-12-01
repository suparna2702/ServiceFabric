package com.similan.service.api.collaborationworkspace.dto.operation;

import java.util.List;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class NewInviteDto extends OperationDto {

  private CollaborationWorkspaceKey workspace;

  private List<SocialActorKey> invitees;

  public NewInviteDto(CollaborationWorkspaceKey workspace,
      List<SocialActorKey> invitees) {
    this.workspace = workspace;
    this.invitees = invitees;
  }

  public CollaborationWorkspaceKey getWorkspace() {
    return workspace;
  }

  public List<SocialActorKey> getInvitees() {
    return invitees;
  }

}
