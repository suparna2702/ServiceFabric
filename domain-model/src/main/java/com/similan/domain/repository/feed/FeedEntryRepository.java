package com.similan.domain.repository.feed;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.activity.News;
import com.similan.domain.entity.feed.ExternalSharedViewFeedEntry;
import com.similan.domain.entity.feed.Feed;
import com.similan.domain.entity.feed.FeedEntry;
import com.similan.domain.entity.feed.InNetworkSharedViewFeedEntry;
import com.similan.domain.entity.feed.NewsFeedEntry;
import com.similan.domain.entity.feed.SurveyRequestFeedEntry;
import com.similan.domain.entity.feed.WallFeedEntry;
import com.similan.domain.entity.poll.PollEvent;
import com.similan.domain.entity.wall.WallEntry;
import com.similan.domain.repository.feed.jpa.JpaFeedEntryRepository;
import com.similan.domain.share.ExternalShared;
import com.similan.domain.share.InNetworkShared;

@Repository
public class FeedEntryRepository {
  @Autowired
  private JpaFeedEntryRepository repository;

  public void delete(Long id) {
    this.repository.delete(id);
  }

  public SurveyRequestFeedEntry find(PollEvent pollEvent) {
    return this.repository.find(pollEvent);
  }

  public FeedEntry save(FeedEntry entity) {
    return repository.save(entity);
  }

  public FeedEntry findOne(Long id) {
    return repository.findOne(id);
  }

  public FeedEntry findOne(String feedOwnerName, int number) {
    return repository.findOne(feedOwnerName, number);
  }

  public List<FeedEntry> findLatestByFeed(String feedOwnerName, Boolean show,
      Pageable pageable) {
    return repository.findLatestByFeed(feedOwnerName, show, pageable);
  }

  public List<FeedEntry> findMoreByFeed(String feedOwnerName,
      int afterNumber, Boolean show, Pageable pageable) {
    return repository.findMoreByFeed(feedOwnerName, afterNumber,
        show, pageable);
  }

  protected int getNextNumber(Feed feed) {
    FeedEntry lastEntry = feed.getLastEntry();
    if (lastEntry == null) {
      return 0;
    }
    int lastNumber = lastEntry.getNumber();
    int nextNumber = lastNumber + 1;
    return nextNumber;
  }

  protected void created(Feed feed, FeedEntry createdEntry) {
    feed.setLastEntry(createdEntry);
  }

  public WallFeedEntry createWallFeedEntry(Feed feed, WallEntry wallEntry) {
    int number = getNextNumber(feed);
    Date date = new Date();
    WallFeedEntry wallFeedEntry = new WallFeedEntry(number, date);

    wallFeedEntry.setWallEntry(wallEntry);
    wallFeedEntry.setFeed(feed);
    created(feed, wallFeedEntry);
    return wallFeedEntry;
  }

  /**
   * Creates a survey feed entry
   * 
   * @param feed
   * @param pollEvent
   * @return
   */
  public SurveyRequestFeedEntry createSurveyRequestFeedEntry(Feed feed,
      PollEvent pollEvent) {

    int number = getNextNumber(feed);
    Date date = new Date();

    SurveyRequestFeedEntry feedEntry = new SurveyRequestFeedEntry(number, date);
    feedEntry.setFeed(feed);
    feedEntry.setPollEvent(pollEvent);
    created(feed, feedEntry);

    return feedEntry;
  }

  /**
   * Creates an external shared feed entry
   * 
   * @param feed
   * @param externalShared
   * @return
   */
  public ExternalSharedViewFeedEntry createExternalSharedViewFeedEntry(
      Feed feed, ExternalShared externalShared) {

    int number = getNextNumber(feed);
    Date date = new Date();

    ExternalSharedViewFeedEntry feedEntry = new ExternalSharedViewFeedEntry(
        number, date);
    feedEntry.setFeed(feed);
    feedEntry.setExternalShared(externalShared);
    created(feed, feedEntry);

    return feedEntry;
  }

  public InNetworkSharedViewFeedEntry createInNetworkSharedViewFeedEntry(
      Feed feed, InNetworkShared inNetworkShared) {

    int number = getNextNumber(feed);
    Date date = new Date();

    InNetworkSharedViewFeedEntry feedEntry = new InNetworkSharedViewFeedEntry(
        number, date);
    feedEntry.setFeed(feed);
    feedEntry.setInNetworkShared(inNetworkShared);
    created(feed, feedEntry);

    return feedEntry;
  }

  public NewsFeedEntry createNewsFeedEntry(Feed feed, News news) {

    int number = getNextNumber(feed);
    Date date = new Date();

    NewsFeedEntry feedEntry = new NewsFeedEntry(number, date, news);
    feedEntry.setFeed(feed);
    created(feed, feedEntry);

    return feedEntry;
  }

}
