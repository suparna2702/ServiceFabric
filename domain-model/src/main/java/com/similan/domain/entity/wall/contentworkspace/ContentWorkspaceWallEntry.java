package com.similan.domain.entity.wall.contentworkspace;

import java.util.Date;

import javax.persistence.Entity;

import com.similan.domain.entity.wall.WallEntry;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "ContentWorkspaceWallEntry")
public abstract class ContentWorkspaceWallEntry extends WallEntry {

  protected ContentWorkspaceWallEntry() {

  }

  public ContentWorkspaceWallEntry(WallEntryType type, int number, Date date) {
    super(type, number, date);
  }

}
