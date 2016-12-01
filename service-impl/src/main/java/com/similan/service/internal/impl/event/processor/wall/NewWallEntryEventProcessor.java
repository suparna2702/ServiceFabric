package com.similan.service.internal.impl.event.processor.wall;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.feed.Feed;
import com.similan.domain.entity.feed.WallFeedEntry;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.WallEntry;
import com.similan.domain.repository.feed.FeedEntryRepository;
import com.similan.service.internal.api.event.io.wall.NewWallEntryEvent;
import com.similan.service.internal.api.feed.FeedInternalService;
import com.similan.service.internal.api.wall.WallInternalService;
import com.similan.service.internal.impl.event.EventPostProcessor;

@Component
public class NewWallEntryEventProcessor<T extends NewWallEntryEvent> implements
    EventPostProcessor<T> {
  @Autowired
  private FeedInternalService feedInternalService;
  @Autowired
  private WallInternalService wallInternalService;
  @Autowired
  private FeedEntryRepository feedEntryRepository;

  @Override
  @Transactional
  public void postProcess(T event) {
    
    long entryId = event.getEntryId();
    WallEntry wallEntry = wallInternalService.getEntry(entryId);
    Wall wall = wallEntry.getWall();
    
    SocialActor initiator = wallEntry.getInitiator();
    Set<SocialActor> consumers = wallInternalService.getConsumers(initiator, wall);
    
    for (SocialActor consumer : consumers) {
      Feed feed = feedInternalService.get(consumer);
      WallFeedEntry feedEntry = feedEntryRepository.createWallFeedEntry(feed,
          wallEntry);
      feedInternalService.post(feedEntry);
    }
  }

}
