package com.similan.service.internal.impl.event.processor.advertisement;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.advertisement.DisplayNotice;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.feed.Feed;
import com.similan.domain.entity.feed.WallFeedEntry;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.advertisement.DisplayNoticeViewedWallEntry;
import com.similan.domain.repository.advertisement.DisplayNoticeRepository;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.domain.repository.feed.FeedEntryRepository;
import com.similan.domain.repository.wall.WallEntryRepository;
import com.similan.service.internal.api.event.io.advertisement.DisplayNoticeViewedEvent;
import com.similan.service.internal.api.feed.FeedInternalService;
import com.similan.service.internal.api.wall.WallInternalService;
import com.similan.service.internal.impl.event.EventPostProcessor;

@Slf4j
@Component
public class DisplayNoticeViewedEventProcessor implements
    EventPostProcessor<DisplayNoticeViewedEvent> {
  @Autowired
  private FeedEntryRepository feedEntryRepository;

  @Autowired
  private FeedInternalService feedInternalService;

  @Autowired
  private SocialActorRepository socialActorRepository;

  @Autowired
  private DisplayNoticeRepository displayNoticeRepository;

  @Autowired
  private WallEntryRepository wallEntryRepository;

  @Autowired
  private WallInternalService wallInternalService;

  @Override
  @Transactional
  public void postProcess(DisplayNoticeViewedEvent event) {
    DisplayNotice notice = this.displayNoticeRepository.findOne(event
        .getNoticeId());
    SocialActor initiator = this.socialActorRepository.findOne(event
        .getViewerId());

    log.info("Display notice view event processed " + event);

    Wall wall = wallInternalService.get(notice.getOwner());

    DisplayNoticeViewedWallEntry wallEntry = this.wallEntryRepository
        .createDisplayNoticeViewedWallEntry(wall, initiator, notice);
    wallEntryRepository.save(wallEntry);

    Feed feed = feedInternalService.get(notice.getCreator());
    WallFeedEntry feedEntry = feedEntryRepository.createWallFeedEntry(feed,
        wallEntry);
    feedInternalService.post(feedEntry);
  }

}
