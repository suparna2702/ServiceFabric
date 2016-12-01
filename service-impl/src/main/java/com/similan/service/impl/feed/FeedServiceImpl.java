package com.similan.service.impl.feed;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.feed.Feed;
import com.similan.domain.entity.feed.FeedEntry;
import com.similan.domain.repository.feed.FeedEntryRepository;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.feed.FeedService;
import com.similan.service.api.feed.dto.basic.FeedDto;
import com.similan.service.api.feed.dto.basic.FeedEntryDto;
import com.similan.service.api.feed.dto.key.FeedKey;
import com.similan.service.impl.base.ServiceImpl;

@Slf4j
@Service
public class FeedServiceImpl extends ServiceImpl implements FeedService {
  private final static int LATEST_COUNT = 10;
  private final static int MORE_COUNT = 20;

  @Autowired
  private FeedEntryRepository feedEntryRepository;
  @Autowired
  private FeedMarshaller feedMarshaller;
  
  @Override
  @Transactional
  public FeedDto get(FeedKey feedKey) {
    Feed feed = feedMarshaller.unmarshallFeedKey(feedKey,
        true);
    FeedDto feedDto = feedMarshaller.marshallFeed(feed);
    return feedDto;
  }

  @Override
  @Transactional
  public List<FeedEntryDto> getLatest(FeedKey feedKey) {
    SocialActorKey feedOwnerKey = feedKey.getOwner();

    String feedOwnerName = feedOwnerKey.getName();
    log.info("Feed owner name " + feedOwnerName);
    
    List<FeedEntry> entries = feedEntryRepository.findLatestByFeed(
        feedOwnerName, Boolean.TRUE, new PageRequest(0, LATEST_COUNT));

    List<FeedEntryDto> entriesDto = feedMarshaller
        .marshallEntriesOfSameFeed(entries);
    return entriesDto;
  }
  

  @Override
  @Transactional
  public List<FeedEntryDto> getMore(FeedKey feedKey,
      Integer afterNumber) {
    SocialActorKey feedOwnerKey = feedKey.getOwner();

    String feedOwnerName = feedOwnerKey.getName();

    List<FeedEntry> entries = feedEntryRepository.findMoreByFeed(feedOwnerName,
        afterNumber, Boolean.TRUE, new PageRequest(0, MORE_COUNT));

    List<FeedEntryDto> entriesDto = feedMarshaller
        .marshallEntriesOfSameFeed(entries);
    return entriesDto;
  }

}
