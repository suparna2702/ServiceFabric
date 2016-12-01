package com.similan.domain.repository.collaborationworkspace;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceSurvey;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.poll.PollEvent;
import com.similan.domain.repository.collaborationworkspace.jpa.JpaCollaborationWorkspaceSurveyRepository;

@Repository
public class CollaborationWorkspaceSurveyRepository {

  @Autowired
  private JpaCollaborationWorkspaceSurveyRepository repository;

  public CollaborationWorkspaceSurvey findOne(Long id) {
    return this.repository.findOne(id);
  }

  public CollaborationWorkspaceSurvey save(CollaborationWorkspaceSurvey survey) {
    return this.repository.save(survey);
  }

  public CollaborationWorkspaceSurvey create(CollaborationWorkspace workspace,
      SocialActor initiator, Date creationDate, PollEvent pollEvent) {

    CollaborationWorkspaceSurvey survey = new CollaborationWorkspaceSurvey(
        creationDate);
    survey.setPollEvent(pollEvent);
    survey.setWorkspace(workspace);
    survey.setCreator(initiator);

    return survey;
  }

}
