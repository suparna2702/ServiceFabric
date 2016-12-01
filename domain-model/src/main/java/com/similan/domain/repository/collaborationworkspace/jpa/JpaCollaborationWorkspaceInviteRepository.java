package com.similan.domain.repository.collaborationworkspace.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceInvite;

public interface JpaCollaborationWorkspaceInviteRepository extends
    JpaRepository<CollaborationWorkspaceInvite, Long> {

  @Query("select invite from CollaborationWorkspaceInvite invite"
      + " where invite.workspace.owner.name = :workspaceOwnerName"
      + " and invite.workspace.name = :workspaceName"
      + " and invite.invitee.name = :inviteeName")
  CollaborationWorkspaceInvite findOne(
      @Param("workspaceOwnerName") String workspaceOwnerName,
      @Param("workspaceName") String workspaceName,
      @Param("inviteeName") String inviteeName);
}
