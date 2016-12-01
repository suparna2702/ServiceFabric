package com.similan.domain.entity.collaborationworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.poll.PollEvent;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "CollaborationWorkspaceSurvey")
@DiscriminatorValue("CollaborationWorkspaceSurvey")
public class CollaborationWorkspaceSurvey extends CollaborationWorkspaceElement {
  
  @ManyToOne(optional = true)
  @JoinColumn(nullable = true)
  private PollEvent pollEvent;

  protected CollaborationWorkspaceSurvey() {

  }

  public CollaborationWorkspaceSurvey(Date created) {
    super(created);
  }

  public PollEvent getPollEvent() {
    return pollEvent;
  }

  public void setPollEvent(PollEvent pollEvent) {
    this.pollEvent = pollEvent;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.COLLABORATION_WORKSPACE_SURVEY;
  }

}
