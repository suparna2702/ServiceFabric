package com.similan.service.api.feed.dto.basic;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.feed.dto.key.FeedEntryKey;

public abstract class FeedEntryDto extends KeyHolderDto<FeedEntryKey> {

  @XmlAttribute
  private Date date;

  protected FeedEntryDto() {
  }

  public FeedEntryDto(FeedEntryKey key, Date date) {
    super(key);
    this.date = date;
  }

  @XmlAttribute
  public abstract FeedEntryType getType();

  public Date getDate() {
    return date;
  }

}
