package com.similan.domain.entity.collaborationworkspace;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class CollaborationWorkspaceSettings {
  
  @Column
  private Boolean showParticipants = Boolean.TRUE;
  
  @Column
  private Boolean showActivity = Boolean.TRUE;

}
