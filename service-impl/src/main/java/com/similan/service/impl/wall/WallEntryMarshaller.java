package com.similan.service.impl.wall;

import java.util.Date;

import com.similan.domain.entity.wall.WallEntry;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public abstract class WallEntryMarshaller<ConcreteWallEntry extends WallEntry, WallContainerKey extends IWallContainerKey> {
  public abstract WallEntryDto<WallContainerKey> marshall(
      WallEntryKey<WallContainerKey> entryKey, ActorDto initiatorDto,
      Date date, ConcreteWallEntry entry);
}
