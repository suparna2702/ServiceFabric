package com.similan.service.api.feed.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

public class FeedEntryKey extends Key {

  @XmlElement
  private FeedKey feed;

  @XmlAttribute
  private Integer number;

  public FeedEntryKey() {
  }

  public FeedEntryKey(String feedOwnerName, Integer number) {
    this(new FeedKey(feedOwnerName), number);
  }

  public FeedEntryKey(FeedKey feed, Integer number) {
    this.feed = feed;
    this.number = number;
  }

  public FeedKey getFeed() {
    return feed;
  }

  public Integer getNumber() {
    return number;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.FEED_ENTRY;
  }

}
