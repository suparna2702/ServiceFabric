package com.similan.service.internal.impl.wall.retriever.wall;

import org.springframework.stereotype.Component;

import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.WallEntry;
import com.similan.service.internal.impl.wall.WallRetriever;

@Component
public class WallEntryWallContainerRetriever implements
    WallRetriever<WallEntry> {
  @Override
  public Wall getWall(WallEntry element) {
    return element.getWall();
  }
}