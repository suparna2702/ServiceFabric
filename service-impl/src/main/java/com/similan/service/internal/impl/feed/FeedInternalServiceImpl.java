package com.similan.service.internal.impl.feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.feed.Feed;
import com.similan.domain.entity.feed.FeedEntry;
import com.similan.domain.repository.feed.FeedEntryRepository;
import com.similan.domain.repository.feed.FeedRepository;
import com.similan.service.api.feed.dto.error.FeedErrorCode;
import com.similan.service.api.feed.dto.error.FeedException;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.internal.api.feed.FeedInternalService;

@Service
public class FeedInternalServiceImpl extends ServiceImpl implements
    FeedInternalService {
  @Autowired
  private FeedRepository feedRepository;
  @Autowired
  private FeedEntryRepository feedEntryRepository;

  @Override
  public void post(FeedEntry entry) {
    feedEntryRepository.save(entry);
  }

  @Override
  public Feed get(SocialActor owner) {
    long ownerId = owner.getId();
    Feed feed = feedRepository.findByOwner(ownerId);
    if (feed == null) {
      feed = feedRepository.create(owner);
      feedRepository.save(feed);

    }
    return feed;
  }

  @Override
  public FeedEntry getEntry(long entryId) {
    FeedEntry entry = feedEntryRepository.findOne(entryId);
    if (entry == null) {
      throw new FeedException(FeedErrorCode.FEED_ENTRY_NOT_FOUND, "No such feed entry: " + entryId);
    }
    return entry;
  }

}
