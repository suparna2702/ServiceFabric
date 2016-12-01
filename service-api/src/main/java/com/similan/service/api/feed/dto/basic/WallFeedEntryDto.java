package com.similan.service.api.feed.dto.basic;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.feed.dto.key.FeedEntryKey;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.key.IWallContainerKey;

public class WallFeedEntryDto<WallContainerKey extends IWallContainerKey>
    extends FeedEntryDto {

  @XmlElement
  private IKeyHolderDto<WallContainerKey> container;

  @XmlElement
  private WallEntryDto<WallContainerKey> wallEntry;

  protected WallFeedEntryDto() {
  }

  public WallFeedEntryDto(FeedEntryKey key, Date date,
      IKeyHolderDto<WallContainerKey> container,
      WallEntryDto<WallContainerKey> wallEntry) {
    super(key, date);
    this.container = container;
    this.wallEntry = wallEntry;
  }

  public FeedEntryType getType() {
    return FeedEntryType.WALL_ENTRY;
  }

  public IKeyHolderDto<WallContainerKey> getContainer() {
    return container;
  }

  public WallEntryDto<WallContainerKey> getWallEntry() {
    return wallEntry;
  }

}
