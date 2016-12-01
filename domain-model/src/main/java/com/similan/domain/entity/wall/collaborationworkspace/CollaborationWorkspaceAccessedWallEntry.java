package com.similan.domain.entity.wall.collaborationworkspace;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.basic.collaborationworkspace.CollaborationWorkspaceAccessedType;

@Entity(name = "CollaborationWorkspaceAccessedWallEntry")
@DiscriminatorValue("COLLABORATION_WORKSPACE_ACCESSED")
public class CollaborationWorkspaceAccessedWallEntry extends
    CollaborationWorkspaceWallEntry {

  @Column
  @Enumerated(EnumType.STRING)
  private CollaborationWorkspaceAccessedType accessType;

  public CollaborationWorkspaceAccessedWallEntry() {

  }

  public CollaborationWorkspaceAccessedWallEntry(int number, Date date) {
    super(WallEntryType.COLLABORATION_WORKSPACE_ACCESSED, number, date);
    this.setShowWall(Boolean.FALSE);
  }

  public CollaborationWorkspaceAccessedType getAccessType() {
    return accessType;
  }

  public void setAccessType(CollaborationWorkspaceAccessedType accessType) {
    this.accessType = accessType;
  }

}
