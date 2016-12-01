package com.similan.service.internal.impl.event.processor.externalshare;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.feed.ExternalSharedViewFeedEntry;
import com.similan.domain.entity.feed.Feed;
import com.similan.domain.repository.feed.FeedEntryRepository;
import com.similan.domain.repository.feed.FeedRepository;
import com.similan.domain.repository.share.ExternalSharedRepository;
import com.similan.domain.share.ExternalShared;
import com.similan.service.internal.api.event.io.externalshare.ExternalShareViewEvent;
import com.similan.service.internal.impl.event.EventPreProcessor;

@Slf4j
@Component
public class ExternalShareViewEventProcessor implements
    EventPreProcessor<ExternalShareViewEvent> {
  @Autowired
  private ExternalSharedRepository externalSharedRepository;
  @Autowired
  private FeedEntryRepository feedEntryRepository;
  @Autowired
  private FeedRepository feedRepository;

  @Override
  public void preProcess(ExternalShareViewEvent event) {
    ExternalShared externalShared = this.externalSharedRepository.findOne(event
        .getExternalShare());
    log.info("External shared " + externalShared.getSharedName());

    Feed feed = this.feedRepository.findByOwner(externalShared.getSharedBy()
        .getId());
    ExternalSharedViewFeedEntry feedEntry = feedEntryRepository
        .createExternalSharedViewFeedEntry(feed, externalShared);
    feedEntryRepository.save(feedEntry);

  }

}
