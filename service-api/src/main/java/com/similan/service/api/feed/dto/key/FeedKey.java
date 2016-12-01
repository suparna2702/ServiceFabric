package com.similan.service.api.feed.dto.key;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class FeedKey extends Key {

  @XmlElement
  private SocialActorKey owner;

  public FeedKey() {
  }

  public FeedKey(String ownerName) {
    this(new SocialActorKey(ownerName));
  }

  public FeedKey(SocialActorKey owner) {
    this.owner = owner;
  }

  public SocialActorKey getOwner() {
    return owner;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.FEED;
  }

}
