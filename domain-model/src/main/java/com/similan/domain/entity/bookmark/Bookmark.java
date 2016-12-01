package com.similan.domain.entity.bookmark;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.community.SocialActor;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "Bookmark")
public class Bookmark implements IDomainEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(nullable = false, length=2000)
  private String name;

  @Column(nullable = false)
  private Date creationDate;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private SocialActor owner;

  private GenericReference<IBookmarkable> bookmarkable;
  
  public Bookmark(){
    
  }

  public Bookmark(String name, Date creationDate) {
    this.name = name;
    this.creationDate = creationDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public SocialActor getOwner() {
    return owner;
  }

  public void setOwner(SocialActor owner) {
    this.owner = owner;
  }

  public GenericReference<IBookmarkable> getBookmarkable() {
    return bookmarkable;
  }

  public void setBookmarkable(GenericReference<IBookmarkable> bookmarkable) {
    this.bookmarkable = bookmarkable;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.BOOKMARK;
  }
}
