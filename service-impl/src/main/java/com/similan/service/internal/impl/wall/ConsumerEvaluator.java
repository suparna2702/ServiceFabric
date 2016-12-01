package com.similan.service.internal.impl.wall;

import java.util.Set;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.IWallContainer;

public interface ConsumerEvaluator<WallContainer extends IWallContainer> {
  Set<SocialActor> getConsumers(SocialActor initiator, WallContainer wallContainer);
}
