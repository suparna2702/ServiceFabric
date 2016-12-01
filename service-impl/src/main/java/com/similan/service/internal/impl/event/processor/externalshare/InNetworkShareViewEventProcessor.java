package com.similan.service.internal.impl.event.processor.externalshare;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.feed.Feed;
import com.similan.domain.entity.feed.InNetworkSharedViewFeedEntry;
import com.similan.domain.repository.feed.FeedEntryRepository;
import com.similan.domain.repository.feed.FeedRepository;
import com.similan.domain.repository.share.InNetworkSharedRepository;
import com.similan.domain.share.InNetworkShared;
import com.similan.service.internal.api.event.io.externalshare.InNetworkShareViewEvent;
import com.similan.service.internal.impl.event.EventPreProcessor;

@Slf4j
@Component
public class InNetworkShareViewEventProcessor implements
    EventPreProcessor<InNetworkShareViewEvent> {
  @Autowired
  private InNetworkSharedRepository inNetworkSharedRepository;
  @Autowired
  private FeedEntryRepository feedEntryRepository;
  @Autowired
  private FeedRepository feedRepository;

  @Override
  public void preProcess(InNetworkShareViewEvent event) {
    InNetworkShared inNetworkShared = this.inNetworkSharedRepository.findOne(event
        .getInNetworkShareShare());
    log.info("External shared " + inNetworkShared.getSharedName());

    Feed feed = this.feedRepository.findByOwner(inNetworkShared.getSharedBy()
        .getId());
    InNetworkSharedViewFeedEntry feedEntry = feedEntryRepository
        .createInNetworkSharedViewFeedEntry(feed, inNetworkShared);
    feedEntryRepository.save(feedEntry);

  }

}
