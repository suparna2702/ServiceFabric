package com.similan.service.api.feed;

import java.util.List;

import com.similan.service.api.feed.dto.basic.FeedDto;
import com.similan.service.api.feed.dto.basic.FeedEntryDto;
import com.similan.service.api.feed.dto.key.FeedKey;

public interface FeedService {

  FeedDto get(FeedKey feedKey);

  List<FeedEntryDto> getLatest(FeedKey feedKey);

  List<FeedEntryDto> getMore(FeedKey feedKey, Integer afterNumber);

}
