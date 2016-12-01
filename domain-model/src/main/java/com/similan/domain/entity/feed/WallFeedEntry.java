package com.similan.domain.entity.feed;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.wall.WallEntry;
import com.similan.service.api.feed.dto.basic.FeedEntryType;

@Entity(name = "WallFeedEntry")
@DiscriminatorValue("WALL_ENTRY")
public class WallFeedEntry extends FeedEntry {

  @ManyToOne(optional = true)
  @JoinColumn(nullable = true)
  private WallEntry wallEntry;

  protected WallFeedEntry() {
  }

  public WallFeedEntry(int number, Date date) {
    super(FeedEntryType.WALL_ENTRY, number, date);
  }

  public WallEntry getWallEntry() {
    return wallEntry;
  }

  public void setWallEntry(WallEntry wallEntry) {
    this.wallEntry = wallEntry;
  }

}
