package com.similan.domain.entity.feed;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.share.InNetworkShared;
import com.similan.service.api.feed.dto.basic.FeedEntryType;

@Entity(name = "InNetworkSharedViewFeedEntry")
@DiscriminatorValue("IN_NETWORK_SHARED_VIEW_ENTRY")
public class InNetworkSharedViewFeedEntry extends FeedEntry {

  @ManyToOne(optional = true)
  @JoinColumn(nullable = true)
  private InNetworkShared inNetworkShared;

  protected InNetworkSharedViewFeedEntry() {

  }

  public InNetworkSharedViewFeedEntry(int number, Date date) {
    super(FeedEntryType.IN_NETWORK_SHARED_VIEW_ENTRY, number, date);
  }

  public InNetworkShared getInNetworkShared() {
    return inNetworkShared;
  }

  public void setInNetworkShared(InNetworkShared inNetworkShared) {
    this.inNetworkShared = inNetworkShared;
  }

}
