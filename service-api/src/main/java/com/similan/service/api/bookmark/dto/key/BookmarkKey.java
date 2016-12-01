package com.similan.service.api.bookmark.dto.key;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

public class BookmarkKey extends Key {

  @XmlAttribute
  private Long id;

  public BookmarkKey() {
  }

  public BookmarkKey(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public EntityType getEntityType() {
    return EntityType.BOOKMARK;
  }

}
