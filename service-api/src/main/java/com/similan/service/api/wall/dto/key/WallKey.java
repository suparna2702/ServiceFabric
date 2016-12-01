package com.similan.service.api.wall.dto.key;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

public class WallKey<WallContainerKey extends IWallContainerKey> extends Key {

  @XmlElement
  private WallContainerKey container;

  public WallKey() {
  }

  public WallKey(WallContainerKey container) {
    this.container = container;
  }

  public WallContainerKey getContainer() {
    return container;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.WALL;
  }

}
