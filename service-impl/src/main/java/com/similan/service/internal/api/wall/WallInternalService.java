package com.similan.service.internal.api.wall;

import java.util.Set;

import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.IWallContainer;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.WallEntry;

public interface WallInternalService {

  void post(WallEntry entry);

  Wall get(IWallContainer container);

  IWallContainer getContainer(Wall wall);

  WallEntry getEntry(long entryId);

  Wall getWall(IDomainEntity element);

  Set<SocialActor> getConsumers(SocialActor initiator, Wall wall);
}
