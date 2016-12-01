package com.similan.service.internal.api.event.io.wall;

import com.similan.service.internal.api.event.io.Event;

public class NewWallEntryEvent extends Event {

  private static final long serialVersionUID = 1L;

  private long entryId;

  public NewWallEntryEvent(long entryId) {
    this.entryId = entryId;
  }

  public long getEntryId() {
    return entryId;
  }

}
