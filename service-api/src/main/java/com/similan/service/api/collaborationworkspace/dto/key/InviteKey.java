package com.similan.service.api.collaborationworkspace.dto.key;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class InviteKey extends Key {

  @XmlElement
  private CollaborationWorkspaceKey workspace;

  @XmlElement
  private SocialActorKey invitee;

  public InviteKey() {
  }

  public InviteKey(String workspaceOwnerName, String workspaceName,
      String inviteeName) {
    this(new CollaborationWorkspaceKey(workspaceOwnerName, workspaceName),
        new SocialActorKey(inviteeName));
  }

  public InviteKey(CollaborationWorkspaceKey workspace, SocialActorKey invitee) {
    super();
    this.workspace = workspace;
    this.invitee = invitee;
  }

  public CollaborationWorkspaceKey getWorkspace() {
    return workspace;
  }

  public SocialActorKey getInvitee() {
    return invitee;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.COLLABORATION_WORKSPACE_INVITE;
  }
}
