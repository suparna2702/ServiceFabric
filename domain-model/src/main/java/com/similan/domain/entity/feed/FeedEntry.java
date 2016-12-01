package com.similan.domain.entity.feed;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.similan.domain.entity.common.IDomainEntity;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.feed.dto.basic.FeedEntryType;

@Entity(name = "FeedEntry")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "feed_id",
    "number" }))
public abstract class FeedEntry implements IDomainEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private Feed feed;

  @Column(nullable = false)
  private int number;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, insertable = false, updatable = false)
  private FeedEntryType type;

  @Column(nullable = false)
  private Date date;

  @Column
  private Boolean showAll = Boolean.TRUE;

  protected FeedEntry() {
  }

  public FeedEntry(FeedEntryType type, int number, Date date) {
    this.type = type;
    this.number = number;
    this.date = date;
    this.showAll = Boolean.TRUE;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.FEED_ENTRY;
  }

  public Integer getNumber() {
    return number;
  }

  public FeedEntryType getType() {
    return type;
  }

  public Feed getFeed() {
    return feed;
  }

  public void setFeed(Feed feed) {
    this.feed = feed;
  }

  public Date getDate() {
    return date;
  }

  public Boolean getShowAll() {
    return showAll;
  }

  public void setShowAll(Boolean show) {
    this.showAll = show;
  }

}
