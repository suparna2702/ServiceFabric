package com.similan.service.internal.api.event.io.contentworkspace;

import lombok.Getter;
import lombok.Setter;

import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.internal.api.event.io.Event;

@Getter
@Setter
public abstract class ContentWorkspaceEvent extends Event {

  private static final long serialVersionUID = 1L;

  private Long contentWorkspaceId;

  private WallEntryType entryType;

  protected ContentWorkspaceEvent() {

  }

  public ContentWorkspaceEvent(Long contentWorkspaceId, WallEntryType entryType) {
    this.contentWorkspaceId = contentWorkspaceId;
    this.entryType = entryType;
  }

}
