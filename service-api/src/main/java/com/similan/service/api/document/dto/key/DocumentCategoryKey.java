package com.similan.service.api.document.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class DocumentCategoryKey extends Key {

  @XmlElement
  private SocialActorKey owner;

  @XmlAttribute
  private String name;

  public DocumentCategoryKey() {
  }

  public DocumentCategoryKey(String ownerName, String name) {
    this(new SocialActorKey(ownerName), name);
  }

  public DocumentCategoryKey(SocialActorKey owner, String name) {
    this.owner = owner;
    this.name = name;
  }

  public SocialActorKey getOwner() {
    return owner;
  }

  public String getName() {
    return name;
  }

  public EntityType getEntityType() {
    return EntityType.DOCUMENT_CATEGORY;
  }

}
