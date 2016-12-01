package com.similan.domain.entity.feed;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.community.activity.News;
import com.similan.service.api.feed.dto.basic.FeedEntryType;

@Entity(name = "NewsFeedEntry")
@DiscriminatorValue("NEWS_FEED_ENTRY")
public class NewsFeedEntry extends FeedEntry {

  @ManyToOne(optional = true)
  @JoinColumn(nullable = true)
  private News news;

  protected NewsFeedEntry() {

  }

  public NewsFeedEntry(int number, Date date, News news) {
    super(FeedEntryType.NEWS_FEED_ENTRY, number, date);
    this.news = news;
  }

  public News getNews() {
    return news;
  }

  public void setNews(News news) {
    this.news = news;
  }

}
