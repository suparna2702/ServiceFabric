package com.similan.domain.repository.collaborationworkspace.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceParticipation;
import com.similan.domain.entity.community.SocialActor;

public interface JpaCollaborationWorkspaceParticipationRepository extends
    JpaRepository<CollaborationWorkspaceParticipation, Long> {

  @Query("select participation from CollaborationWorkspaceParticipation participation"
      + " where participation.workspace.owner.name = :workspaceOwnerName"
      + " and participation.workspace.name = :workspaceName"
      + " and participation.participant.name = :participantName")
  CollaborationWorkspaceParticipation findOne(
      @Param("workspaceOwnerName") String workspaceOwnerName,
      @Param("workspaceName") String workspaceName,
      @Param("participantName") String participantName);

  @Query("select participation from CollaborationWorkspaceParticipation participation"
      + " where participation.participant = :participant" +
      " order by participation.workspace.name, participation.workspace.owner.name")
  List<CollaborationWorkspaceParticipation> findByParticipant(
      @Param("participant") SocialActor participant);

  CollaborationWorkspaceParticipation findByWorkspaceAndParticipant(
      CollaborationWorkspace workspace, SocialActor participant);

}
