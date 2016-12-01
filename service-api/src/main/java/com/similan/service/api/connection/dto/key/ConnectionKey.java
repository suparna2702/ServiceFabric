package com.similan.service.api.connection.dto.key;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class ConnectionKey extends Key {

  @XmlElement
  private SocialActorKey owner;

  @XmlElement
  private SocialActorKey contact;

  public ConnectionKey() {
  }

  public ConnectionKey(String ownerName, String contactName) {
    this(new SocialActorKey(ownerName), new SocialActorKey(contactName));
  }

  public ConnectionKey(SocialActorKey owner, SocialActorKey contact) {
    this.owner = owner;
    this.contact = contact;
  }

  public SocialActorKey getOwner() {
    return owner;
  }

  public SocialActorKey getContact() {
    return contact;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.CONNECTION;
  }

}
