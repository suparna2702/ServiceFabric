package com.similan.domain.repository.managementworkspace;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.entity.managementworkspace.ManagementWorkspaceParticipation;
import com.similan.domain.repository.managementworkspace.jpa.JpaManagementWorkspaceParticipationRepository;

@Repository
public class ManagementWorkspaceParticipationRepository {

  @Autowired
  private JpaManagementWorkspaceParticipationRepository repository;

  public ManagementWorkspaceParticipation save(
      ManagementWorkspaceParticipation pert) {
    return this.repository.save(pert);
  }

  public ManagementWorkspaceParticipation findOne(Long id) {
    return this.repository.findOne(id);
  }

  public List<ManagementWorkspaceParticipation> findAll() {
    return this.repository.findAll();
  }

  public List<ManagementWorkspaceParticipation> findByParticipant(
      SocialActor participant) {
    return this.repository.findByParticipant(participant);
  }

  public List<ManagementWorkspaceParticipation> findByWorkspace(
      ManagementWorkspace workspace) {
    return repository.findByWorkspace(workspace);
  }

  public ManagementWorkspaceParticipation findByWorkspaceAndParticipant(
      ManagementWorkspace workspace, SocialActor participant) {
    return this.repository
        .findByWorkspaceAndParticipant(workspace, participant);
  }

  public Long participantCount(ManagementWorkspace workspace) {
    return this.repository.participantCount(workspace);
  }

  public ManagementWorkspaceParticipation create(SocialActor perticipant,
      ManagementWorkspace workspace) {
    ManagementWorkspaceParticipation perticipation = new ManagementWorkspaceParticipation();
    perticipation.setJoinDate(new Date());
    perticipation.setManagementWorkspace(workspace);
    perticipation.setPerticipant(perticipant);

    return perticipation;
  }
}
