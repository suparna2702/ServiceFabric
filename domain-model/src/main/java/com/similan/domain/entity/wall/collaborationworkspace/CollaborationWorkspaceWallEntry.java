package com.similan.domain.entity.wall.collaborationworkspace;

import java.util.Date;

import javax.persistence.Entity;

import com.similan.domain.entity.wall.WallEntry;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "CollaborationWorkspaceWallEntry")
public abstract class CollaborationWorkspaceWallEntry extends WallEntry {

  protected CollaborationWorkspaceWallEntry() {
  }

  public CollaborationWorkspaceWallEntry(WallEntryType type, int number,
      Date date) {
    super(type, number, date);
  }

}
