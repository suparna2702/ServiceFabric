package com.similan.service.api.community.dto.key;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.bookmark.dto.key.IBookmarkableKey;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;

public class SocialActorKey extends Key implements IWallContainerKey,
    IWallEntrySubjectKey,IBookmarkableKey {

  @XmlAttribute
  private String name;

  public SocialActorKey() {
  }

  public SocialActorKey(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public EntityType getEntityType() {
    return EntityType.SOCIAL_ACTOR;
  }

  public void setName(String name) {
    this.name = name;
  }

}
