package com.similan.service.api.product.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class ProductKey extends Key {

  @XmlElement
  private SocialActorKey owner;

  @XmlAttribute
  private String name;

  public ProductKey() {
  }

  public ProductKey(String ownerName, String name) {
    this(new SocialActorKey(ownerName), name);
  }

  public ProductKey(SocialActorKey owner, String name) {
    this.name = name;
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public SocialActorKey getOwner() {
    return owner;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.PRODUCT;
  }

}
