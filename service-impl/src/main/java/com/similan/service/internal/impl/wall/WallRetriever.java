package com.similan.service.internal.impl.wall;

import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.wall.Wall;

public interface WallRetriever<T extends IDomainEntity> {
  Wall getWall(T element);
}
