package com.similan.domain.repository.managementworkspace.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.entity.managementworkspace.ManagementWorkspaceParticipation;

public interface JpaManagementWorkspaceParticipationRepository extends
    JpaRepository<ManagementWorkspaceParticipation, Long> {

  List<ManagementWorkspaceParticipation> findByParticipant(
      SocialActor participant);

  List<ManagementWorkspaceParticipation> findByWorkspace(
      ManagementWorkspace workspace);

  ManagementWorkspaceParticipation findByWorkspaceAndParticipant(
      ManagementWorkspace workspace, SocialActor participant);

  @Query("select count(participation) from ManagementWorkspaceParticipation participation"
      + " where participation.workspace = :workspace")
  Long participantCount(@Param("workspace") ManagementWorkspace workspace);

}
