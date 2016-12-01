package com.similan.domain.repository.collaborationworkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceInvite;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.repository.collaborationworkspace.jpa.JpaCollaborationWorkspaceInviteRepository;

@Repository
public class CollaborationWorkspaceInviteRepository {
  @Autowired
  private JpaCollaborationWorkspaceInviteRepository repository;

  public CollaborationWorkspaceInvite save(
      CollaborationWorkspaceInvite invite) {
    return repository.save(
      invite);
  }

  public CollaborationWorkspaceInvite findOne(Long id) {
    return repository.findOne(id);
  }

  public CollaborationWorkspaceInvite findOne(
      String workspaceOwnerName, String workspaceName, String inviteeName) {
    return repository.findOne(
      workspaceOwnerName, workspaceName, inviteeName);
  }

  public CollaborationWorkspaceInvite create(
      CollaborationWorkspace workspace, SocialActor inviter, SocialActor invitee) {
    CollaborationWorkspaceInvite invite = new CollaborationWorkspaceInvite();
    invite.setWorkspace(workspace);
    invite.setInviter(inviter);
    invite.setInvitee(invitee);

    return invite;
  }
}
