package com.similan.domain.repository.collaborationworkspace;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceParticipation;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.repository.collaborationworkspace.jpa.JpaCollaborationWorkspaceParticipationRepository;

@Repository
public class CollaborationWorkspaceParticipationRepository {
  @Autowired
  private JpaCollaborationWorkspaceParticipationRepository repository;

  public CollaborationWorkspaceParticipation save(
      CollaborationWorkspaceParticipation participation) {
    return repository.save(
      participation);
  }

  public CollaborationWorkspaceParticipation findOne(Long id) {
    return repository.findOne(id);
  }

  public CollaborationWorkspaceParticipation findOne(
      String workspaceOwnerName, String workspaceName, String participantName) {
    return repository.findOne(
      workspaceOwnerName, workspaceName, participantName);
  }

  public List<CollaborationWorkspaceParticipation> findByParticipant(
      SocialActor participant) {
    return repository.findByParticipant(
      participant);
  }
  
  public CollaborationWorkspaceParticipation create(
      CollaborationWorkspace workspace, SocialActor participant, Date joinDate) {
    CollaborationWorkspaceParticipation participation = new CollaborationWorkspaceParticipation(
        joinDate);
    participation.setParticipant(participant);
    participation.setWorkspace(workspace);
    return participation;
  }

  public CollaborationWorkspaceParticipation findByWorkspaceAndParticipant(
      CollaborationWorkspace workspace, SocialActor participant) {
    return repository.findByWorkspaceAndParticipant(workspace, participant);
  }
}
