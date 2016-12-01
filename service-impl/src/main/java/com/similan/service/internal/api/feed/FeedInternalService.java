package com.similan.service.internal.api.feed;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.feed.Feed;
import com.similan.domain.entity.feed.FeedEntry;

public interface FeedInternalService {

  void post(FeedEntry entry);

  Feed get(SocialActor owner);

  FeedEntry getEntry(long entryId);

}
