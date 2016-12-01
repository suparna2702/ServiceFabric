package com.similan.domain.entity.collaborationworkspace;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "CollaborationWorkspaceParticipation")
public class CollaborationWorkspaceParticipation implements IDomainEntity, 
                    IWallEntrySubject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private SocialActor participant;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private CollaborationWorkspace workspace;

  @Column(nullable = false)
  private Date joinDate;

  protected CollaborationWorkspaceParticipation() {
  }

  public CollaborationWorkspaceParticipation(Date joinDate) {
    this.joinDate = joinDate;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.COLLABORATION_WORKSPACE_PARTICIPATION;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public SocialActor getParticipant() {
    return participant;
  }

  public void setParticipant(SocialActor participant) {
    this.participant = participant;
  }

  public CollaborationWorkspace getWorkspace() {
    return workspace;
  }

  public void setWorkspace(CollaborationWorkspace workspace) {
    this.workspace = workspace;
  }

  public Date getJoinDate() {
    return joinDate;
  }

}
