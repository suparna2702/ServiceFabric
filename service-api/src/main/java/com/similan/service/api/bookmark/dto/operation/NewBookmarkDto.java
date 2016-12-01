package com.similan.service.api.bookmark.dto.operation;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.bookmark.dto.key.IBookmarkableKey;
import com.similan.service.api.community.dto.key.SocialActorKey;

@XmlRootElement
public class NewBookmarkDto extends OperationDto {
  @XmlElement
  private SocialActorKey owner;

  @XmlElement
  private IBookmarkableKey bookmarkable;

  @XmlElement
  private String name;

  public NewBookmarkDto() {
  }

  public NewBookmarkDto(SocialActorKey owner, IBookmarkableKey bookmarkable,
      String name) {
    this.name = name;
    this.owner = owner;
    this.bookmarkable = bookmarkable;
  }

  public IBookmarkableKey getBookmarkable() {
    return bookmarkable;
  }

  public SocialActorKey getOwner() {
    return owner;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "NewBookmarkDto [owner=" + owner + ", bookmarkable=" + bookmarkable
        + ", name=" + name + "]";
  }

}
