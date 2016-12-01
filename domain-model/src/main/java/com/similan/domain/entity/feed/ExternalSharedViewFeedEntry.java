package com.similan.domain.entity.feed;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.share.ExternalShared;
import com.similan.service.api.feed.dto.basic.FeedEntryType;

@Entity(name = "ExternalSharedViewFeedEntry")
@DiscriminatorValue("EXTERNAL_SHARED_VIEW_ENTRY")
public class ExternalSharedViewFeedEntry extends FeedEntry {

  @ManyToOne(optional = true)
  @JoinColumn(nullable = true)
  private ExternalShared externalShared;

  protected ExternalSharedViewFeedEntry() {

  }

  public ExternalSharedViewFeedEntry(int number, Date date) {
    super(FeedEntryType.EXTERNAL_SHARED_VIEW_ENTRY, number, date);
  }

  public ExternalShared getExternalShared() {
    return externalShared;
  }

  public void setExternalShared(ExternalShared externalShared) {
    this.externalShared = externalShared;
  }

}
