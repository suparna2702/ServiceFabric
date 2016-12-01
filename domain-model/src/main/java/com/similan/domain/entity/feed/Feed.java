package com.similan.domain.entity.feed;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.community.SocialActor;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "Feed")
public class Feed implements IDomainEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @OneToOne(optional = false)
  @JoinColumn(nullable = false)
  private SocialActor owner;

  @OneToMany(mappedBy = "feed")
  @OrderBy("date desc, number desc")
  private List<FeedEntry> entries;

  @OneToOne(optional = true)
  @JoinColumn(nullable = true)
  private FeedEntry lastEntry;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.FEED;
  }

  public SocialActor getOwner() {
    return owner;
  }

  public void setOwner(SocialActor owner) {
    this.owner = owner;
  }

  public List<FeedEntry> getEntries() {
    return entries;
  }

  public void setLastEntry(FeedEntry lastEntry) {
    this.lastEntry = lastEntry;
  }

  public FeedEntry getLastEntry() {
    return lastEntry;
  }
}
