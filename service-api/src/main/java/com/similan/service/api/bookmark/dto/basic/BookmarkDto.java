package com.similan.service.api.bookmark.dto.basic;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.bookmark.dto.key.BookmarkKey;

public class BookmarkDto extends KeyHolderDto<BookmarkKey> {

  @XmlAttribute
  private Date creationDate;

  @XmlAttribute
  private String name;

  @XmlAttribute
  private String bookmarkUrl;

  @XmlAttribute
  private EntityType entityType;

  protected BookmarkDto() {
  }

  public BookmarkDto(BookmarkKey key, EntityType entityType, Date creationDate,
      String name, String bookmarkUrl) {
    super(key);
    this.creationDate = creationDate;
    this.name = name;
    this.bookmarkUrl = bookmarkUrl;
    this.entityType = entityType;
  }

  public EntityType getEntityType() {
    return entityType;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public String getName() {
    return name;
  }

  public String getBookmarkUrl() {
    return bookmarkUrl;
  }

  @Override
  public String toString() {
    return "BookmarkDto [creationDate=" + creationDate + ", name=" + name
        + ", bookmarkUrl=" + bookmarkUrl + ", entityType=" + entityType + "]";
  }

}
