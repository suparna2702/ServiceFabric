package com.similan.service.api.wall.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

public class WallEntryKey<WallContainerKey extends IWallContainerKey> extends
    Key {

  @XmlElement
  private WallKey<WallContainerKey> wall;

  @XmlAttribute
  private Integer number;

  public WallEntryKey() {
  }

  public WallEntryKey(WallContainerKey wallContainer, Integer number) {
    this(new WallKey<WallContainerKey>(wallContainer), number);
  }

  public WallEntryKey(WallKey<WallContainerKey> wall, Integer number) {
    this.wall = wall;
    this.number = number;
  }

  public WallKey<WallContainerKey> getWall() {
    return wall;
  }

  public Integer getNumber() {
    return number;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.WALL_ENTRY;
  }

}
