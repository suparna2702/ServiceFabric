package com.similan.service.api.collaborationworkspace.dto.key;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;

public class CollaborationWorkspaceParticipationKey extends Key implements IWallEntrySubjectKey{

  @XmlElement
  private CollaborationWorkspaceKey workspace;

  @XmlElement
  private SocialActorKey participant;

  public CollaborationWorkspaceParticipationKey() {
  }

  public CollaborationWorkspaceParticipationKey(String workspaceOwnerName,
      String workspaceName, String participantName) {
    this(new CollaborationWorkspaceKey(workspaceOwnerName, workspaceName),
        new SocialActorKey(participantName));
  }

  public CollaborationWorkspaceParticipationKey(
      CollaborationWorkspaceKey workspace, SocialActorKey participant) {
    this.workspace = workspace;
    this.participant = participant;
  }

  public CollaborationWorkspaceKey getWorkspace() {
    return workspace;
  }

  public SocialActorKey getParticipant() {
    return participant;
  }

  public EntityType getEntityType() {
    return EntityType.COLLABORATION_WORKSPACE_PARTICIPATION;
  }

}
