package com.similan.domain.repository.wall;

import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.WallEntry;

@Repository
public class AbstractWallEntryRepository<T extends WallEntry> {

  protected int getNextNumber(Wall wall) {
    WallEntry lastEntry = wall.getLastEntry();
    if (lastEntry == null) {
      return 0;
    }
    int lastNumber = lastEntry.getNumber();
    int nextNumber = lastNumber + 1;
    return nextNumber;
  }

  protected void created(Wall wall, SocialActor initiator, T creatadEntry) {
    creatadEntry.setWall(wall);
    creatadEntry.setInitiator(initiator);
    wall.setLastEntry(creatadEntry);
  }

}
