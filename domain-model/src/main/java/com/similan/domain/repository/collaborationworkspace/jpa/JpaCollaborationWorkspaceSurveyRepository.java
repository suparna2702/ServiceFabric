package com.similan.domain.repository.collaborationworkspace.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceSurvey;

public interface JpaCollaborationWorkspaceSurveyRepository extends
    JpaRepository<CollaborationWorkspaceSurvey, Long> {

}
